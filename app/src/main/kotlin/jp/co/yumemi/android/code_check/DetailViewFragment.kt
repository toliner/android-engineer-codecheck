/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.databinding.FragmentDetailBinding

class DetailViewFragment : Fragment(R.layout.fragment_detail) {

    private val args: DetailViewFragmentArgs by navArgs()

    private lateinit var _binding: FragmentDetailBinding
    private val binding: FragmentDetailBinding
        get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailBinding.bind(view)

        val item = args.item

        binding.ownerIconView.load(item.ownerIconUrl)
        binding.nameView.text = item.name
        binding.languageView.text = item.language
        binding.starsView.text = "${item.stargazersCount} stars"
        binding.watchersView.text = "${item.watchersCount} watchers"
        binding.forksView.text = "${item.forksCount} forks"
        binding.openIssuesView.text = "${item.openIssuesCount} open issues"
    }
}
