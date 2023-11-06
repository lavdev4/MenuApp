package com.example.menuapp.presentation.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.menuapp.R
import com.example.menuapp.databinding.FragmentMenuBinding
import com.example.menuapp.presentation.MainActivity
import com.example.menuapp.presentation.adapters.CategoriesAdapter
import com.example.menuapp.presentation.adapters.MealsAdapter
import com.example.menuapp.presentation.viewmodels.AppViewModelFactory
import com.example.menuapp.presentation.viewmodels.MenuViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<MenuViewModel> { viewModelFactory }
    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding
        get() = _binding ?: throw RuntimeException("FragmentMenuBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainActivitySubcomponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        if (checkForInternetConnection(context)) {
//            viewModel.deleteData()
//            viewModel.loadData()
//        }
        observeCategories()
        observeMeals()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeCategories() {
        val categoriesAdapter = CategoriesAdapter(context) {
            viewModel.setSelectedCategory(it)
        }
        with(binding.categories) {
            val orientation = RecyclerView.HORIZONTAL
            val divider = DividerItemDecoration(context, orientation)
            val dividerDrawable = ContextCompat
                .getDrawable(context, R.drawable.divider_categories_list)
            layoutManager = LinearLayoutManager(
                context,
                orientation,
                false
            )
            dividerDrawable?.let { divider.setDrawable(it) }
            addItemDecoration(divider)
            adapter = categoriesAdapter
        }
        lifecycleScope.launch {
            viewModel.getCategories()
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .collect {
                    Log.d("app", "categories: " + it.size)
                    categoriesAdapter.submitList(it)
                }
        }
    }

    private fun observeMeals() {
        val mealsAdapter = MealsAdapter()
        with(binding.mealsList) {
            val orientation = RecyclerView.VERTICAL
            val divider = DividerItemDecoration(context, orientation)
            val dividerDrawable = ContextCompat
                .getDrawable(context, R.drawable.divider_meals_list)
            layoutManager = LinearLayoutManager(
                context,
                orientation,
                false
            )
            dividerDrawable?.let { divider.setDrawable(it) }
            addItemDecoration(divider)
            adapter = mealsAdapter
        }
        lifecycleScope.launch {
            viewModel.getMeals()
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .collect {
                    Log.d("app", "meals: " + it.size)
                    mealsAdapter.submitList(it)
                }
        }
    }

    private fun checkForInternetConnection(context: Context?): Boolean {
        context ?: throw RuntimeException("Connection check requested before Context was created.")
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}