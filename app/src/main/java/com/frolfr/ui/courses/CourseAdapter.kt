package com.frolfr.ui.courses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.frolfr.domain.model.Course
import com.frolfr.databinding.ViewCourseItemBinding

class CourseAdapter(private val clickListener: CourseClickListener) :
    ListAdapter<CourseWithLastPlayed, CourseItemViewHolder>(CourseDiffCallback()) {

    override fun onBindViewHolder(holder: CourseItemViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItemViewHolder {
        return CourseItemViewHolder.from(parent)
    }
}

class CourseItemViewHolder private constructor(private val binding: ViewCourseItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        course: CourseWithLastPlayed,
        clickListener: CourseClickListener
    ) {
        binding.courseLastPlayed = course
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CourseItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewCourseItemBinding.inflate(layoutInflater, parent, false)
            return CourseItemViewHolder(binding)
        }
    }
}

class CourseDiffCallback : DiffUtil.ItemCallback<CourseWithLastPlayed>() {
    override fun areItemsTheSame(oldItem: CourseWithLastPlayed, newItem: CourseWithLastPlayed): Boolean {
        return oldItem.course.id == newItem.course.id
    }

    override fun areContentsTheSame(oldItem: CourseWithLastPlayed, newItem: CourseWithLastPlayed): Boolean {
        return oldItem == newItem
    }
}

class CourseClickListener(val clickListener: (course: Course) -> Unit) {
    fun onClick(course: Course) = clickListener(course);
}