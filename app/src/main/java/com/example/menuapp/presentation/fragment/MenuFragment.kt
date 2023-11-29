package com.example.menuapp.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.menuapp.domain.entities.LoadResultEntity
import com.example.menuapp.presentation.MainActivity
import com.example.menuapp.presentation.adapters.CategoriesAdapter
import com.example.menuapp.presentation.adapters.MealsAdapter
import com.example.menuapp.presentation.viewmodels.AppViewModelFactory
import com.example.menuapp.presentation.viewmodels.MenuViewModel
import kotlinx.coroutines.flow.collectLatest
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
        super.onViewCreated(view, savedInstanceState)
        observeDataLoad()
        observeCategories()
        observeMeals()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeDataLoad() {
        viewModel.dataLoadObservable.observe(viewLifecycleOwner) {
            when (it) {
                LoadResultEntity.LoadStarted -> showProgress(true)
                LoadResultEntity.Loaded -> showProgress(false)
                is LoadResultEntity.Failed -> showToast(getString(R.string.error_toast_text, it.message))
                else -> {}
            }
        }
    }

    private fun observeCategories() {
        val adapter = createCategoriesAdapter()
        lifecycleScope.launch {
            viewModel.categoriesFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .collectLatest { adapter.submitList(it) }
        }
    }

    private fun observeMeals() {
        val adapter = createMealsAdapter()
        lifecycleScope.launch {
            viewModel.mealsFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                .collectLatest { adapter.submitList(it) }
        }
    }

    private fun createCategoriesAdapter(): CategoriesAdapter {
        val categoriesAdapter = CategoriesAdapter(
            colorTextSelected = getColor(R.color.pink),
            colorBackgroundSelected = getColor(R.color.light_pink),
            colorTextUnselected = getColor(R.color.light_gray),
            colorBackgroundUnselected = getColor(R.color.white)
        ) { viewModel.setSelectedCategory(it) }
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
        return categoriesAdapter
    }

    private fun createMealsAdapter(): MealsAdapter {
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
        return mealsAdapter
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    private fun showProgress(visible: Boolean) {
        binding.progress.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun getColor(colorResource: Int): Int {
        return ContextCompat.getColor(requireContext(), colorResource)
    }
}