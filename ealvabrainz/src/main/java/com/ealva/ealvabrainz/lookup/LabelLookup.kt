/*
 * Copyright (c) 2021  Eric A. Snell
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

package com.ealva.ealvabrainz.lookup

import com.ealva.ealvabrainz.brainz.data.Label
import com.ealva.ealvabrainz.common.QueryMap
import com.ealva.ealvabrainz.common.buildQueryMap
import com.ealva.ealvabrainz.common.include
import com.ealva.ealvabrainz.common.status
import com.ealva.ealvabrainz.common.types

public interface LabelLookup : EntitySubqueryLookup<Label.Include> {
  public companion object {
    public operator fun invoke(lookup: LabelLookup.() -> Unit): QueryMap =
      LabelLookupOp().apply(lookup).queryMap()
  }
}

private class LabelLookupOp : BaseSubqueryLookup<Label.Include>(), LabelLookup {
  override fun validateInclude(set: Set<Label.Include>) {
    set.ifContainsThenRequires(Label.Include.DiscIds, Label.Include.Releases)
    set.ifContainsThenRequires(Label.Include.Media, Label.Include.Releases)
    set.ifContainsThenRequires(Label.Include.ArtistCredits, Label.Include.Releases)
  }

  fun queryMap(): QueryMap = buildQueryMap {
    val incSet = allIncludes
    include(incSet)
    types(typeSet?.ensureValidType(incSet))
    status(statusSet?.ensureValidStatus(incSet))
  }
}
