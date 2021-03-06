/*
 * Copyright (c) 2020  Eric A. Snell
 *
 * This file is part of eAlvaBrainz
 *
 * eAlvaBrainz is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 * eAlvaBrainz is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaBrainz.
 * If not, see <http://www.gnu.org/licenses/>.
 */

@file:Suppress("Indentation", "MagicNumber")

package com.ealva.brainzapp.ui.artist

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.view.isVisible
import androidx.lifecycle.whenStarted
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ealva.brainzapp.ui.fragment.FragmentUiContext
import com.ealva.brainzapp.ui.fragment.Navigation
import com.ealva.brainzapp.ui.view.addCircularProgress
import com.ealva.brainzapp.ui.view.addOnTouchOvalRipple
import com.ealva.brainzapp.ui.view.clickFlow
import com.ealva.brainzapp.ui.view.hideKeyboard
// import com.ealva.brainzapp.ui.view.snackErrors
import com.ealva.brainzapp.R
import com.ealva.brainzapp.ui.view.snackErrors
import com.google.android.material.textfield.TextInputLayout
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import com.mikepenz.iconics.utils.paddingDp
import com.mikepenz.iconics.utils.sizeDp
import fr.castorflex.android.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import splitties.dimensions.dip
import splitties.experimental.ExperimentalSplittiesApi
import splitties.snackbar.snack
import splitties.views.InputType
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.coordinatorlayout.coordinatorLayout
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.add
import splitties.views.dsl.core.endMargin
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.margin
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.startMargin
import splitties.views.dsl.core.verticalMargin
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.material.MaterialComponentsStyles
import splitties.views.dsl.material.addInput
import splitties.views.dsl.recyclerview.recyclerView
import com.ealva.brainzapp.R.id.artist_search_circular_progress as ID_CIRCULAR_PROGRESS
import com.ealva.brainzapp.R.id.artist_search_constraint as ID_CONSTRAINT
import com.ealva.brainzapp.R.id.artist_search_coordinator as ID_COORDINATOR
import com.ealva.brainzapp.R.id.artist_search_recycler as ID_RECYCLER
import com.ealva.brainzapp.R.id.artist_search_search_button as ID_SEARCH_BUTTON
import com.ealva.brainzapp.R.id.artist_search_search_text as ID_SEARCH_TEXT
import com.ealva.brainzapp.R.id.artist_search_text_input_layout as ID_TEXT_INPUT_LAYOUT

interface ArtistSearchUi : Ui {

  companion object {
    fun make(
      uiContext: FragmentUiContext,
      viewModel: ArtistSearchViewModel,
      navigation: Navigation
    ): ArtistSearchUi {
      return ArtistSearchUiImpl(uiContext, viewModel, navigation)
    }
  }
}

private class ArtistSearchUiImpl(
  private val uiContext: FragmentUiContext,
  private val viewModel: ArtistSearchViewModel,
  private val navigation: Navigation
) : ArtistSearchUi {
  override val ctx = uiContext.context
  private val scope = uiContext.scope
  private val lifecycleOwner = uiContext.lifecycleOwner

  private val textInputLayout: TextInputLayout
  private val searchBtn: ImageView
  private val recycler: RecyclerView
  private val searchItemAdapter: ArtistSearchItemAdapter
  private val progress: CircularProgressBar

  private val materialStyles = MaterialComponentsStyles(ctx)

  @OptIn(ExperimentalSplittiesApi::class, ExperimentalCoroutinesApi::class)
  override val root = coordinatorLayout(ID_COORDINATOR) {
    add(constraintLayout(ID_CONSTRAINT) {

      textInputLayout = add(materialStyles.textInputLayout.filledBox(ID_TEXT_INPUT_LAYOUT) {
        addInput(ID_SEARCH_TEXT) {
          setHint(R.string.artist_search_hint)
          inputType = InputType.text.value
          scope.launch {
            lifecycleOwner.whenStarted {
              setOnEditorActionListener { _, _, _ ->
                doSearch(text?.toString())
                true
              }
            }
          }
        }
      }, lParams(height = wrapContent) {
        startToStart = PARENT_ID
        topToTop = PARENT_ID
        endToStart = ID_SEARCH_BUTTON
        bottomToTop = ID_RECYCLER
        margin = dip(4)
        startMargin = dip(8)
      })

      searchBtn = add(imageView(ID_SEARCH_BUTTON) {
        setImageDrawable(IconicsDrawable(context, MaterialDesignIconic.Icon.gmi_search).apply {
          sizeDp = 40
          paddingDp = 8
        })
        addOnTouchOvalRipple()
      }, lParams(width = dip(40), height = dip(40)) {
        startToEnd = ID_TEXT_INPUT_LAYOUT
        topToTop = ID_TEXT_INPUT_LAYOUT
        endToEnd = PARENT_ID
        bottomToBottom = ID_TEXT_INPUT_LAYOUT
        verticalMargin = dip(4)
        endMargin = dip(4)
      })

      recycler = add(recyclerView(ID_RECYCLER) {
        setHasFixedSize(true)
        adapter = ArtistSearchItemAdapter(uiContext) { selection ->
          navigation.push(
            ArtistFragment.make(uiContext.fragmentManager, selection.mbid, selection.name),
            ArtistFragment.NAME
          )
        }.also { searchItemAdapter = it }
        layoutManager = LinearLayoutManager(context)
      }, lParams {
        startToStart = PARENT_ID
        topToBottom = ID_TEXT_INPUT_LAYOUT
        endToEnd = PARENT_ID
        bottomToBottom = PARENT_ID
        bottomMargin = dip(4)
      })
    }, defaultLParams(matchParent, matchParent) { }
    )

    progress = addCircularProgress(ID_CIRCULAR_PROGRESS)
  }.also { root ->
    scope.launch {
      lifecycleOwner.whenStarted {
        searchBtn.clickFlow().onEach {
          doSearch(textInputLayout.editText?.text?.toString())
        }.launchIn(scope)

        viewModel.itemList.observe(uiContext.lifecycleOwner, { list ->
          if (list != null && list.isNotEmpty()) {
            searchItemAdapter.setItems(list)
          }
          recycler.layoutManager?.scrollToPosition(0)
        })

        viewModel.unsuccessful.snackErrors(uiContext.lifecycleOwner, root)

        viewModel.isBusy.observe(uiContext.lifecycleOwner, { busy ->
          progress.isVisible = busy == true
        })
      }
    }
  }

  private fun doSearch(query: String?) {
    uiContext.fragment.requireActivity().hideKeyboard()
    if (!query.isNullOrBlank()) {
      viewModel.findArtist(query)
    } else {
      root.snack(R.string.query_field_empty)
    }
  }
}
