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
class UserListActivity : AppCompatActivity() {

    private val viewModel: UserListViewModel by viewModels()
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
        binding.list.onScrollBottom { viewModel.fetch() }

        lifecycleScope.launchWhenStarted {
            viewModel.userListModels
                .collect(adapter::submitList)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.errorMessage
                .collect(::showToast)
        }
    }
}
