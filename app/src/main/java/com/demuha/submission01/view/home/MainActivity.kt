package com.demuha.submission01.view.home

import android.app.Activity
import androidx.core.util.Pair
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.demuha.submission01.R
import com.demuha.submission01.databinding.ActivityMainBinding
import com.demuha.submission01.model.StoryDto
import com.demuha.submission01.util.Resource
import com.demuha.submission01.util.formatDate
import com.demuha.submission01.view.ViewModelFactory
import com.demuha.submission01.view.home.ui.LoadingStateAdapter
import com.demuha.submission01.view.home.ui.StoryAdapter
import com.demuha.submission01.view.story.AddStoryActivity
import com.demuha.submission01.view.story.StoryDetailActivity
import com.demuha.submission01.view.story.StoryMapActivity
import com.demuha.submission01.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.sessions.observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                observeStories()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        observeStories()
        onMenuTopBar()
        setupView()
    }

    private fun onMenuTopBar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle("Alert")
                        setMessage("Yakin?")
                        setPositiveButton("Keluar") { _, _ ->
                            viewModel.logout()
                        }
                        setNegativeButton("Tutup") {_,_ ->}
                        create()
                        show()
                    }
                    true
                }
                R.id.action_map -> {
                    val intent = Intent(this@MainActivity, StoryMapActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeStories() {
        showLoading(true)
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.stories.observe(this@MainActivity) {
            showLoading(false)
            adapter.submitData(lifecycle, it)
            adapter.setOnItemClickCallback { item, view ->
                navigateToDetail(item, view)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun navigateToDetail(story: StoryDto, itemView: View) {

        val intent = Intent(this, StoryDetailActivity::class.java).apply {
            putExtra(EXTRA_ID, story.id)
            putExtra(EXTRA_NAME, story.name)
            putExtra(EXTRA_DESC, story.description)
            putExtra(EXTRA_PHOTO, story.photoUrl)
            putExtra(EXTRA_CREATED, formatDate(story.createdAt))
        }
        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                itemView.context as Activity,
                Pair(itemView.findViewById(R.id.iv_item_photo), "detailPhoto"),
                Pair(itemView.findViewById(R.id.tv_item_name), "detailName"),
                Pair(itemView.findViewById(R.id.tv_description), "detailDesc"),
                Pair(itemView.findViewById(R.id.tv_date), "detailDate"),
            )
        startActivity(intent, optionsCompat.toBundle())
    }

    private fun showError(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_CREATED = "extra_created"
    }
}