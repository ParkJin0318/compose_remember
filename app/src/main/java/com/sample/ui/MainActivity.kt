package com.sample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        val adapter = UserListAdapter()
        val layoutManager = LinearLayoutManager(this)

        binding.list.adapter = adapter
        binding.list.layoutManager = layoutManager
        binding.list.onScrollBottom { viewModel.load() }

        viewModel.userListModels.observe(this, adapter::submitList)
        viewModel.onFailure.observe(this) { showToast(it) }
    }
}
