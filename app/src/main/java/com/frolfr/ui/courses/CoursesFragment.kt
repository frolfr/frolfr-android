package com.frolfr.ui.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.frolfr.R
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.Course
import com.frolfr.databinding.FragmentCoursesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 10

class CoursesFragment : Fragment() {

    private lateinit var coursesViewModel: CoursesViewModel

    private lateinit var binding: FragmentCoursesBinding

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCoursesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_courses, container, false
        )

        coursesViewModel =
            ViewModelProviders.of(this).get(CoursesViewModel::class.java)

        binding.coursesViewModel = coursesViewModel

        val courseAdapter = CourseAdapter()

        // TODO needed, or can I use binding?
        coursesViewModel.courses.observe(viewLifecycleOwner, Observer {
            courseAdapter.courses =
                (if (coursesViewModel.courses.value == null) emptyList()
                else coursesViewModel.courses.value) as List<Course>
        })

        binding.userCoursesList.adapter = courseAdapter
        binding.userCoursesList.setOnClickListener { view ->
            // TODO is this for each item, or the view group?
        }

        getCourses(1)

        return binding.root
    }

    private fun getCourses(page: Int) {
        coroutineScope.launch {
            try {
                val userCoursesResponse = FrolfrApi.retrofitService.userCourses(page, PAGE_SIZE)
                coursesViewModel.courses.value = userCoursesResponse.courses
            } catch (t: Throwable) {
                Log.i("frolfrUserCourses", "Got error result", t)
            }
        }
    }
}
