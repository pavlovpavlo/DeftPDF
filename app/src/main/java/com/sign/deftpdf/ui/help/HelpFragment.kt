package com.sign.deftpdf.ui.help

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.databinding.FragmentHelpBinding
import com.sign.deftpdf.model.faq.FaqData
import com.sign.deftpdf.model.faq.FaqModel
import com.sign.deftpdf.ui.main.MainActivity

class HelpFragment : Fragment(R.layout.fragment_help), HelpView {

    private val binding by viewBinding(FragmentHelpBinding::bind)
    private var list: MutableList<FaqData> = listOf<FaqData>().toMutableList()
    private var adapter: HelpAdapter = HelpAdapter(list)
    private lateinit var presenter: HelpPresenter
    private lateinit var activity: MainActivity
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            activity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = HelpPresenter(activity)
        presenter.attachView(this)
        presenter.sendResponse()
        navController = NavHostFragment.findNavController(this@HelpFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener { navController.popBackStack() }
        binding.questions.layoutManager = LinearLayoutManager(requireContext())
        binding.questions.adapter = adapter
    }

    override fun requestSuccess(data: FaqModel) {
        list = data.data as MutableList<FaqData>
        adapter.setData(list)
    }

}