package com.example.fragmentargument

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.fragmentargument.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    var binding: FragmentImageBinding? = null
    val model: MyViewModel by activityViewModels()
    val imglist = arrayListOf(R.drawable.extinction, R.drawable.glacier, R.drawable.gas)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            imageView.setOnClickListener{
                val fragment = requireActivity().supportFragmentManager.beginTransaction()
                fragment.addToBackStack(null)
                val textFragment = TextFragment()
                fragment.replace(R.id.frameLayout, textFragment)
                fragment.commit()
            }
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    R.id.radioButton1 -> {
                        model.setLiveData(0)
                    }
                    R.id.radioButton2 -> {
                        model.setLiveData(1)
                    }
                    R.id.radioButton3 -> {
                        model.setLiveData(2)
                    }
                }
                imageView.setImageResource(imglist[model.selectedNum.value!!])
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}