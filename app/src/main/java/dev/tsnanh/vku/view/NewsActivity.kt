package dev.tsnanh.vku.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        val navArgs: NewsActivityArgs by navArgs()

        actionBar?.apply {
            title = navArgs.title
            subtitle = "http://daotao.sict.udn.vn" + navArgs.url
        }
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }
}
