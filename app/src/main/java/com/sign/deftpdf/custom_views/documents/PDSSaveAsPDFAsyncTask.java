package com.sign.deftpdf.custom_views.documents;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.sign.deftpdf.ui.view_document.DocumentViewerActivity;
import com.sign.deftpdf.util.ViewUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class PDSSaveAsPDFAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private String mfileName;
    DocumentViewerActivity mCtx;


    public PDSSaveAsPDFAsyncTask(DocumentViewerActivity context, String str) {
        this.mCtx = context;
        this.mfileName = str;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    public Boolean doInBackground(Void... voidArr) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        PDSPDFDocument document = mCtx.getDocument();
        File root = new File(mCtx.getExternalFilesDir(null).getAbsolutePath(), "pdfsdcard_location");

        if (!root.exists()) {
            root.mkdirs();
        }
        String fileName = mfileName.replace(".pdf", "") + ".pdf";
        File file = new File(root, fileName);

        if (file.exists())
            file.delete();
        try {
            InputStream stream = document.stream;
            FileOutputStream os = new FileOutputStream(file);
            PdfReader reader = new PdfReader(stream);
            PdfStamper signer = null;
            Bitmap createBitmap = null;
            for (int i = 0; i < document.getNumPages(); i++) {
                Rectangle mediabox = reader.getPageSize(i + 1);
                for (int j = 0; j < document.getPage(i).getNumElements(); j++) {
                    PDSPDFPage page = document.getPage(i);
                    PDSElement element = page.getElement(j);
                    RectF bounds = element.getRect();
                    if (element.getType() == PDSElement.PDSElementType.PDSElementTypeSignature) {
                        PDSElementViewer viewer = element.mElementViewer;
                        View dummy = viewer.getElementView();
                        View view = ViewUtils.createSignatureView(mCtx, element, viewer.mPageViewer.getToViewCoordinatesMatrix());
                        createBitmap = Bitmap.createBitmap(dummy.getWidth(), dummy.getHeight(), Bitmap.Config.ARGB_8888);
                        view.draw(new Canvas(createBitmap));
                    } else {
                        createBitmap = element.getBitmap();
                    }
                    ByteArrayOutputStream saveBitmap = new ByteArrayOutputStream();
                    createBitmap.compress(Bitmap.CompressFormat.PNG, 100, saveBitmap);
                    byte[] byteArray = saveBitmap.toByteArray();
                    createBitmap.recycle();

                    Image sigimage = Image.getInstance(byteArray);
                    if (signer == null)
                        signer = new PdfStamper(reader, os, '\0');
                    PdfContentByte contentByte = signer.getOverContent(i + 1);
                    sigimage.setAlignment(Image.ALIGN_UNDEFINED);
                    sigimage.scaleToFit(bounds.width(), bounds.height());
                    sigimage.setAbsolutePosition(bounds.left - (sigimage.getScaledWidth() - bounds.width()) / 2, mediabox.getHeight() - (bounds.top + bounds.height()));
                    contentByte.addImage(sigimage);
                }
            }
            if (signer != null)
                signer.close();
            if (reader != null)
                reader.close();
            if (os != null)
                os.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (file.exists()) {
                file.delete();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onPostExecute(Boolean result) {

        if (!result)
            Toast.makeText(mCtx, "Something went wrong while Signing PDF document, Please try again", Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(mCtx, "PDF document saved successfully", Toast.LENGTH_LONG).show();
            mCtx.runPostExecution();
        }

    }
}

