package com.sign.deftpdf.ui.documents_screens.home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BasicView
import com.sign.deftpdf.databinding.FragmentHomeBinding
import com.sign.deftpdf.model.documents.DocumentData
import com.sign.deftpdf.model.documents.DocumentsModel
import com.sign.deftpdf.ui.documents_screens.DocumentsAdapter
import com.sign.deftpdf.ui.documents_screens.DocumentsPresenter
import com.sign.deftpdf.ui.documents_screens.DocumentsView
import com.sign.deftpdf.ui.main.MainActivity
import com.sign.deftpdf.util.Util
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home), DocumentsView {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var presenter: DocumentsPresenter
    private var page: Int = 1
    private var perPage: Int = 50
    private lateinit var documentModel: DocumentsModel
    private var list: List<DocumentData> = listOf()
    private var adapter: DocumentsAdapter = DocumentsAdapter(listOf())
    private val currentDate: String =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    Date()
            )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = DocumentsPresenter((activity as MainActivity))
        presenter.attachView(this)
        presenter.sendResponse(DeftApp.user.apiToken!!,
                page.toString(), perPage.toString(),
                Util.SORT_BY, DeftApp.sortTypeHome,
                Util.calculateDate(DeftApp.filterTypeHome), currentDate, null)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            documentsList.layoutManager = LinearLayoutManager(context)
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
        val newList = mutableListOf<DocumentData>()
        for (item in list) {
            item.originalName?.let {
                if (it.contains(text)) {
                    newList.add(item)
                }
            }
        }
        adapter.setDocuments(newList)
    }

}