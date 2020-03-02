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

package com.ealva.brainzapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.ealva.brainzapp.ui.artist.ArtistFragment
import com.ealva.brainzapp.ui.artist.ArtistSearchFragment
import com.ealva.brainzapp.ui.fragment.Navigation
import com.ealva.brainzapp.ui.release.ReleaseSearchFragment
import com.ealva.brainzapp.ui.rgroup.ReleaseGroupSearchFragment
import com.ealva.brainzsvc.service.MusicBrainzService
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import timber.log.Timber

typealias FragmentFactoryFn = () -> Fragment

class AppFragmentFactory(
  override val kodein: Kodein,
  navigation: Navigation
) : FragmentFactory(), KodeinAware {
  private val brainz: MusicBrainzService by instance()

  private val factories: MutableMap<String, FragmentFactoryFn> = mutableMapOf(
    MainSearchFragment.NAME to { MainSearchFragment.make() },
    ArtistSearchFragment.NAME to { ArtistSearchFragment.make(brainz, navigation) },
    ReleaseGroupSearchFragment.NAME to { ReleaseGroupSearchFragment.make(brainz, navigation) },
    ReleaseSearchFragment.NAME to { ReleaseSearchFragment.make(brainz, navigation) },
    ArtistFragment.NAME to { ArtistFragment(brainz, navigation) }
  )

  /**
   * Creates the [Fragment], injecting necessary dependencies via ctor
   */
  override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
    return factories[className]?.let { it() } ?: makeViaReflection(classLoader, className)
  }

  private fun makeViaReflection(classLoader: ClassLoader, className: String): Fragment {
    Timber.w("No factory for %s", className)
    return super.instantiate(classLoader, className)
  }
}

inline fun <reified T : Fragment> FragmentManager.instantiate(args: Bundle? = null): T {
  return (fragmentFactory.instantiate(
    T::class.java.classLoader!!,
    T::class.java.name
  ) as T).apply { if (args != null) arguments = args }
}
