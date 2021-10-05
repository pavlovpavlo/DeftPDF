package com.sign.deftpdf.ui.documents_screens.library

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.app.ShareCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseFragment
import com.sign.deftpdf.base.BaseModelView
import com.sign.deftpdf.databinding.FragmentLibraryBinding
import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.model.documents.DocumentsData
import com.sign.deftpdf.model.documents.DocumentsModel
import com.sign.deftpdf.ui.documents_screens.*
import com.sign.deftpdf.ui.documents_screens.delete.DeleteDocumentDialog
import com.sign.deftpdf.ui.documents_screens.document.DocumentDetailsActivity
import com.sign.deftpdf.ui.documents_screens.rename.RenameDocumentDialog
import com.sign.deftpdf.ui.main.MainActivity
import com.sign.deftpdf.ui.request_signature.RequestSignatureActivity
import com.sign.deftpdf.util.Util
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.*

class LibraryFragment : BaseFragment(R.layout.fragment_library), DocumentsView, DocumentsAdapter.OnDocumentClickListener {

    private val binding by viewBinding(FragmentLibraryBinding::bind)

    private lateinit var presenter: DocumentsPresenter
    private lateinit var navController: NavController
    private lateinit var activity: MainActivity
    private var page: Int = 1
    private var perPage: Int = 50
    private lateinit var documentModel: DocumentsModel
    private var list: MutableList<DocumentsData> = listOf<DocumentsData>().toMutableList()
    private var adapter: DocumentsAdapter = DocumentsAdapter(listOf())
    private val currentDate: String =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    Date()
            )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            activity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = NavHostFragment.findNavController(this@LibraryFragment)
        presenter = DocumentsPresenter(activity)
        presenter.attachView(this)
        sendRequest()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    fun sendRequest() {
        presenter.sendResponse(DeftApp.user.apiToken!!,
                page.toString(), perPage.toString(),
                Util.SORT_BY, DeftApp.sortTypeLibrary,
                Util.calculateDate(DeftApp.filterTypeLibrary), currentDate, "original")
    }

    private fun showDetailDialog(position: Int) {
        val data: DocumentsData = list[position]
        val view: View = showDocumentDetailPopup()

        val documentName: TextView = view.findViewById(R.id.document_name)
        val documentDate: TextView = view.findViewById(R.id.document_date)
        val documentDetail: ImageView = view.findViewById(R.id.document_detail)
        val documentImage: ImageView = view.findViewById(R.id.document_image)
        val rename: LinearLayout = view.findViewById(R.id.rename_document)
        val delete: LinearLayout = view.findViewById(R.id.delete_document)
        val details: LinearLayout = view.findViewById(R.id.detail_document)
        val sentToEmail: LinearLayout = view.findViewById(R.id.email_document)
        val export: LinearLayout = view.findViewById(R.id.export_document)
        val signDocument: LinearLayout = view.findViewById(R.id.sign_document)
        val requestDocument: LinearLayout = view.findViewById(R.id.request_document)

        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        calendar.time = sdf.parse(data.updatedAt)

        sdf.timeZone = TimeZone.getDefault()
        val date = sdf.format(calendar.time)

        documentDetail.visibility = View.GONE
        documentName.text = data.originalName
        documentDate.text = date
        documentImage.setImageResource(when (data.status) {
            "original" -> {
                R.drawable.ic_document_original
            }
            "signed" -> {
                R.drawable.ic_document_signed
            }
            "pending" -> {
                R.drawable.ic_document_pending
            }
            "draft" -> {
                R.drawable.ic_document_draft
            }
            else ->
                R.drawable.ic_document_original
        })

        if (data.status == "signed" || data.status == "pending") {
            signDocument.visibility = View.GONE
            requestDocument.visibility = View.GONE
            sentToEmail.setOnClickListener {
                val selectorIntent = Intent(Intent.ACTION_SENDTO)
                selectorIntent.data = Uri.parse("mailto:")

                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(DeftApp.user.email))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, data.originalName)
                emailIntent.putExtra(Intent.EXTRA_TEXT, Util.DATA_URL+data.originalDocument)
                emailIntent.selector = selectorIntent

                activity.startActivity(Intent.createChooser(emailIntent, "Send email..."))
                dismissPopup()
            }
            export.setOnClickListener {
                ShareCompat.IntentBuilder.from(activity)
                        .setType("text/plain")
                        .setChooserTitle("Share URL")
                        .setText(Util.DATA_URL + data.originalDocument)
                        .startChooser()
                dismissPopup()
            }
        } else {
            sentToEmail.visibility = View.GONE
            export.visibility = View.GONE
            signDocument.setOnClickListener {
                activity.openDocumentShow(data)
                dismissPopup()
            }
            requestDocument.setOnClickListener {
                val intent = Intent(activity, RequestSignatureActivity::class.java)
                intent.putExtra("document", data)
                startActivity(intent)
                dismissPopup()
            }
        }

        details.setOnClickListener {
            DocumentDetailsActivity.listener = object : OnDocumentDetailListener{
                override fun documentDeleted() {
                    list.removeAt(position)
                    adapter.setDocuments(list)
                    binding.count.text = list.size.toString()
                }

                override fun documentUpdate(newName: String) {
                    list[position].originalName = newName
                    adapter.notifyItemChanged(position)
                }

            }
            val intent = Intent(activity, DocumentDetailsActivity::class.java)
            intent.putExtra("document", data)
            startActivity(intent)
            dismissPopup()
        }

        rename.setOnClickListener {
            renameDocument(position)
            dismissPopup()
        }
        delete.setOnClickListener {
            deleteDocument(position)
            dismissPopup()
        }
    }

    private fun renameDocument(position: Int) {
        RenameDocumentDialog.display(activity.supportFragmentManager) { newName ->
            run {
                sendRequestRename(position, newName)
            }
        }
    }

    private fun sendRequestRename(position: Int, newName: String) {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("", "")
                .build().part(0)
        val updatePresenter = DocumentsUpdatePresenter(activity)
        updatePresenter.attachView(object : BaseModelView {
            override fun requestSuccess(data: BaseModel) {
                list[position].originalName = newName
                adapter.notifyItemChanged(position)
            }
        })
        updatePresenter.sendResponse(
                DeftApp.user.apiToken!!,
                list[position].id.toString(),
                multipartBody, null, newName)
    }

    private fun deleteDocument(position: Int) {
        DeleteDocumentDialog.display(activity.supportFragmentManager,
                list[position].originalName.toString()) {
            sendRequestDelete(position)
        }
    }

    private fun sendRequestDelete(position: Int) {
        val deletePresenter = DocumentsDeletePresenter(activity)
        deletePresenter.attachView(object : BaseModelView {
            override fun requestSuccess(data: BaseModel) {
                list.removeAt(position)
                adapter.setDocuments(list)
                binding.count.text = list.size.toString()
            }
        })
        deletePresenter.sendResponse(
                DeftApp.user.apiToken!!,
                list[position].id.toString())
    }

    private fun initListeners() {
        with(binding) {
            documentsList.layoutManager = LinearLayoutManager(context)
            adapter.listener = this@LibraryFragment
            documentsList.adapter = adapter
            searchEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filter(searchEdit.text.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            burgerBtn.setOnClickListener { openMenu() }
            sortMenu.setOnClickListener { showSortDialog() }
            filterMenu.setOnClickListener { showFilterDialog() }
            notificationMenu.setOnClickListener { navController.navigate(R.id.navigation_notification) }
        }
    }

    private fun showSortDialog() {
        val view: View = showSortPopup()
        val sortGroup = view.findViewById<RadioGroup>(R.id.sort_container)
        val reset = view.findViewById<LinearLayout>(R.id.reset)
        reset.setOnClickListener {
            DeftApp.sortTypeLibrary = Util.SORT_DESC
            list.clear()
            sendRequest()
            dismissPopup()
        }
        sortGroup.check(
                if (DeftApp.sortTypeLibrary == Util.SORT_DESC)
                    R.id.latest_sort
                else
                    R.id.oldest_sort)

        sortGroup.setOnCheckedChangeListener { group, checkedId ->
            DeftApp.sortTypeLibrary = if (checkedId == R.id.latest_sort)
                Util.SORT_DESC
            else
                Util.SORT_ASC
            list.clear()
            sendRequest()
            dismissPopup()
        }
    }

    private fun showFilterDialog() {
        val view: View = showFilterPopup()
        val filterGroup = view.findViewById<RadioGroup>(R.id.filter_container)
        val reset = view.findViewById<LinearLayout>(R.id.reset)
        reset.setOnClickListener {
            DeftApp.sortTypeLibrary = Util.SORT_DESC
            list.clear()
            sendRequest()
            dismissPopup()
        }
        filterGroup.check(
                if (DeftApp.filterTypeLibrary == Util.FILTER_WEEK)
                    R.id.week_filter
                else
                    R.id.month_filter)

        filterGroup.setOnCheckedChangeListener { group, checkedId ->
            DeftApp.filterTypeLibrary = if (checkedId == R.id.week_filter)
                Util.FILTER_WEEK
            else
                Util.FILTER_MONTH
            list.clear()
            sendRequest()
            dismissPopup()
        }
    }

    override fun requestSuccess(data: DocumentsModel) {
        documentModel = data
        data.data?.let {
            adapter.setDocuments(it)
            list = it
            binding.count.text = it.size.toString()
        }
    }

    private fun filter(text: String) {
        val newList = mutableListOf<DocumentsData>()
        for (item in list) {
            item.originalName?.let {
                if (it.toLowerCase().contains(text.toLowerCase())) {
                    newList.add(item)
                }
            }
        }
        binding.count.text = newList.size.toString()
        adapter.setDocuments(newList)
    }

    override fun onDetailClick(position: Int) {
        showDetailDialog(position)
    }

    override fun onItemClick(position: Int) {
        activity.openDocumentShow(list[position])
    }

}