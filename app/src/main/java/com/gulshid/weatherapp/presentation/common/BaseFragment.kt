package com.gulshid.weatherapp.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Generic base fragment that handles ViewBinding boilerplate.
 *
 * How to use in a subclass:
 *
 *   class HomeFragment : BaseFragment<FragmentHomeBinding>() {
 *       override fun inflateBinding(...) = FragmentHomeBinding.inflate(inflater, container, false)
 *       override fun onViewCreated(...) { binding.myTextView.text = "Hello" }
 *   }
 *
 * [VB] is the generated ViewBinding class for your layout XML.
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    // _binding is nullable — it's null before onCreateView and after onDestroyView
    private var _binding: VB? = null

    // 'binding' is the safe non-null accessor used inside onViewCreated
    protected val binding get() = _binding!!

    /**
     * Each fragment implements this to inflate its own layout.
     * Android Studio will generate [FragmentXxxBinding] from your XML automatically.
     */
    abstract fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    /**
     * Override this in your fragments to set up UI, observe LiveData, etc.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    /** Set up click listeners, adapters, UI initialization here */
    open fun setupViews() {}

    /** Observe LiveData / StateFlow from ViewModel here */
    open fun observeViewModel() {}

    // IMPORTANT: Always null out binding here to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}