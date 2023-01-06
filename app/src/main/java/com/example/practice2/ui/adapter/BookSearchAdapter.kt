package com.example.practice2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.practice2.data.model.Book
import com.example.practice2.databinding.ItemBookPreviewBinding

class BookSearchAdapter : ListAdapter<Book, BookSearchAdapter.BookSearchViewHolder>(BookDiffCallback) {
    inner class BookSearchViewHolder(private val binding : ItemBookPreviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book : Book) {
            val author = book.authors.toString().removeSurrounding("[","]")
            val publisher = book.publisher
            val date = if (book.datetime.isNotEmpty()) book.datetime.substring(0,10) else ""

            itemView.apply {
                binding.ivArticleImage.load(book.thumbnail)
                binding.tvTitle.text = book.title
                binding.tvAuthor.text = "$author ㅣ $publisher"
                binding.tvDatetime.text = date
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSearchViewHolder {
        return BookSearchViewHolder(
            ItemBookPreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }
    override fun onBindViewHolder(holder: BookSearchViewHolder, position: Int) {
        val book = currentList[position]
        holder.bind(book)
    }

    companion object {
        private val BookDiffCallback = object  : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.isbn == newItem.isbn
            }
            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }
}
