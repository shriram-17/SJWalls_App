package com.nerdinfusions.sjwalls.ui.adapters

import android.view.ViewGroup
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter
import com.afollestad.sectionedrecyclerview.SectionedViewHolder
import com.nerdinfusions.sjwalls.R
import com.nerdinfusions.sjwalls.data.models.AboutItem
import com.nerdinfusions.sjwalls.extensions.views.inflate
import com.nerdinfusions.sjwalls.ui.viewholders.AboutViewHolder
import com.nerdinfusions.sjwalls.ui.viewholders.SectionHeaderViewHolder

class AboutAdapter(
    private val internalAboutItems: ArrayList<AboutItem>,
    internalAboutItems1: ArrayList<AboutItem>
) : SectionedRecyclerViewAdapter<SectionedViewHolder>() {

    init {
        shouldShowHeadersForEmptySections(false)
        shouldShowFooters(false)
    }

    override fun getItemCount(section: Int): Int = when (section) {
        1 -> internalAboutItems.size
        else -> 0
    }

    override fun getItemViewType(section: Int, relativePosition: Int, absolutePosition: Int): Int =
        section

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionedViewHolder =
        when (viewType) {
            0, 1 -> AboutViewHolder(parent.inflate(R.layout.item_about))
            else -> SectionHeaderViewHolder(parent.inflate(R.layout.item_section_header))
        }

    override fun onBindHeaderViewHolder(
        holder: SectionedViewHolder?,
        section: Int,
        expanded: Boolean
    ) {
        val titleRes = when (section) {
            0 -> R.string.app_name
            1 -> R.string.dashboard
            else -> 0
        }
        (holder as? SectionHeaderViewHolder)?.bind(titleRes, 0, section > 0 && getItemCount(0) > 0)
    }

    override fun onBindViewHolder(
        holder: SectionedViewHolder?,
        section: Int,
        relativePosition: Int,
        absolutePosition: Int
    ) {
        if (section >= 2) return
        (holder as? AboutViewHolder)?.bind(
            if (section == 0) internalAboutItems.getOrNull(relativePosition) else TODO()
        )
    }

    override fun onBindFooterViewHolder(holder: SectionedViewHolder?, section: Int) {}
    override fun getSectionCount(): Int = 2
}