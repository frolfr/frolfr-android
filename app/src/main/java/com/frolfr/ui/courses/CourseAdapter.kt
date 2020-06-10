package com.frolfr.ui.courses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.frolfr.api.model.Course
import com.frolfr.databinding.UserCourseViewBinding
import java.text.DateFormat
import java.text.SimpleDateFormat

class CourseAdapter(private val listEndListener: ListEndListener) :
    ListAdapter<Course, CourseItemViewHolder>(UserCourseDiffCallback()) {

    override fun onBindViewHolder(holder: CourseItemViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
        if (position == (itemCount - 1)) {
            listEndListener.onListEnd()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItemViewHolder {
        return CourseItemViewHolder.from(parent)
    }

}

abstract class ListEndListener {
    abstract fun onListEnd();
}

class CourseItemViewHolder private constructor(private val binding: UserCourseViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(course: Course) {
        binding.userCourse = course
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CourseItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = UserCourseViewBinding.inflate(layoutInflater, parent, false)
            return CourseItemViewHolder(binding)
        }
    }
}

class UserCourseDiffCallback : DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }
}