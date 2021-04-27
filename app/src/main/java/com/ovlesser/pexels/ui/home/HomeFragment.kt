package com.ovlesser.pexels.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ovlesser.pexels.R
import com.ovlesser.pexels.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private var keyword = ""
    private val homeViewModel: HomeViewModel by lazy {
        val activity = requireNotNull( this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, HomeViewModel.Factory(activity.application))
            .get(HomeViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val binding = FragmentHomeBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = homeViewModel

        binding.photosGrid.adapter = PhotoGridAdapter() {homeViewModel.refreshRepository()}

        return binding.root
    }

    private var searchView: androidx.appcompat.widget.SearchView? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        searchView = menu.findItem(R.id.action_search)?.let {
            it.actionView as? androidx.appcompat.widget.SearchView
        }?.apply {
            (activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager)?.also {
                setSearchableInfo(it.getSearchableInfo(activity?.componentName))
            }
            maxWidth = Int.MAX_VALUE

            setIconifiedByDefault(false)
            isIconified = false
            isSubmitButtonEnabled = false
            setOnQueryTextListener(this@HomeFragment)
            requestFocus()
            layoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return query?.let {
            if (it.length < 2) { return@let false }
            keyword = it
            homeViewModel.updateKeyword(keyword)
            searchView?.clearFocus()
            true
        } ?: false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}