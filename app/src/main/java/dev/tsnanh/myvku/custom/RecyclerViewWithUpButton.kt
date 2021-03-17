package dev.tsnanh.myvku.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.myvku.databinding.RecyclerviewWithUpButtonBinding
import dev.tsnanh.myvku.utils.isMPlus

class RecyclerViewWithUpButton<VH : RecyclerView.ViewHolder> @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttrs) {
    private var isInitialized = false
    private var binding: RecyclerviewWithUpButtonBinding =
        RecyclerviewWithUpButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.scrollToTop.translationX = 256F
        binding.scrollToTop.setOnClickListener { binding.list.smoothScrollToPosition(0) }
        with(binding.list) {
            if (isMPlus()) {
                setOnScrollChangeListener { v, _, _, _, _ ->
                    showScrollToTopButton((v as RecyclerView).computeVerticalScrollOffset() > 2000)
                }
            } else {
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(
                        recyclerView: RecyclerView,
                        newState: Int
                    ) {
                        showScrollToTopButton(recyclerView.computeVerticalScrollOffset() > 2000)
                    }
                })
            }
        }
    }

    private fun showScrollToTopButton(isVisible: Boolean) {
        binding.scrollToTop.animate()
            .translationX(if (isVisible) 0F else 256F)
            .setDuration(300L)
            .setListener(null)
    }

    val list get() = binding.list

    var layoutManager = binding.list.layoutManager
        set(value) {
            field = value
            binding.list.layoutManager = value
        }
}