package com.frolfr.ui.courses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.frolfr.domain.model.Course
import com.frolfr.databinding.ViewCourseItemBinding

class CourseAdapter(private val clickListener: CourseClickListener) :
    ListAdapter<Course, CourseItemViewHolder>(CourseDiffCallback()) {

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
        course: Course,
        clickListener: CourseClickListener
    ) {
        binding.course = course
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

class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }
}

class CourseClickListener(val clickListener: (course: Course) -> Unit) {
    fun onClick(course: Course) = clickListener(course);
}