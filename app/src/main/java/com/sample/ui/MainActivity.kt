package com.sample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.R
import com.sample.databinding.ActivityMainBinding
import com.sample.extension.onScrollBottom
import com.sample.extension.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()

        val adapter = UserListAdapter()
        val layoutManager = LinearLayoutManager(this)

        binding.list.adapter = adapter
        binding.list.layoutManager = layoutManager
        binding.list.onScrollBottom(2) { viewModel.fetch() }

        lifecycleScope.launchWhenResumed {
            viewModel.state.collect {
                when (it) {
                    is MainUiState.Success -> {
                        adapter.submitList(it.list)
                    }
                    is MainUiState.Failure -> {
                        showToast(it.message)
                    }
                }
            }
        }
    }
}
