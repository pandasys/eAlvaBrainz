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

package com.ealva.ealvabrainz.brainz.data

import com.ealva.ealvabrainz.brainz.data.CoverArtArchive.Companion.NullCoverArtArchive
import com.ealva.ealvabrainz.brainz.data.Release.Companion.NullRelease
import com.ealva.ealvabrainz.moshi.FallbackOnNull
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import timber.log.Timber

/**
 * A MusicBrainz release represents the unique release (i.e. issuing) of a product on a specific
 * date with specific release information such as the country, label, barcode and packaging. If you
 * walk into a store and purchase an album or single, they are each represented in MusicBrainz as
 * one release.
 *
 * Each release belongs to a release group and contains at least one medium (commonly referred to
 * as a disc when talking about a CD release). Each medium has a tracklist.
 *
 * A medium is the actual physical medium that stores the audio content. This means that each CD in
 * a multi-disc release will be entered as separate mediums within the release, and that both sides
 * of a vinyl record or cassette will exist on one medium. Mediums have a format (e.g. CD, DVD,
 * vinyl, and cassette) and can optionally also have a title. Sometimes a medium can be a side of a
 * disc. For example, the two sides of a hybrid SACD (the CD side and the SACD side) should be
 * entered as two mediums.
 *
 * Tracklists represent the set and ordering of tracks as listed on a liner, and the same tracklist
 * can appear on more than one release. For example, a boxset compilation that contains previously
 * released CDs would share the same tracklists as the separate releases.
 *
 * [MusicBrainz Release](https://musicbrainz.org/doc/Release)
 */
@JsonClass(generateAdapter = true)
public class Release(
  /** The MusicBrainz ID (MBID) */
  public var id: String = "",
  /** The title of the release. */
  public var title: String = "",
  /** The date the release was issued */
  public var date: String = "",
  @field:Json(name = "release-group") @field:FallbackOnNull
  public var releaseGroup: ReleaseGroup = ReleaseGroup.NullReleaseGroup,
  @field:Json(name = "release-events") public var releaseEvents: List<ReleaseEvent> = emptyList(),
  /** The artist(s) that the release is primarily credited to, as credited on the release. */
  @field:Json(name = "artist-credit") public var artistCredit: List<ArtistCredit> = emptyList(),
  /**
   * The status describes how "official" a release is. Possible values are:
   * * **official**
   * Any release officially sanctioned by the artist and/or their record company. Most releases will
   * fit into this category.
   * * **promotional**
   * A give-away release or a release intended to promote an upcoming official release
   * (e.g. pre-release versions, releases included with a magazine, versions supplied to radio DJs
   * for air-play).
   * * **bootleg**
   * An unofficial/underground release that was not sanctioned by the artist and/or the record
   * company. This includes unofficial live recordings and pirated releases.
   * * **pseudo-release**
   * An alternate version of a release where the titles have been changed. These don't correspond
   * to any real release and should be linked to the original release using the transliteration
   * relationship.
   */
  public var status: String = "",
  /**
   * The name of the physical packaging that accompanies the release. See the
   * [list of packaging](https://musicbrainz.org/doc/Release/Packaging) for more information.
   */
  public var packaging: String = "",
  /**
   * The ID of the physical packaging
   */
  @field:Json(name = "packaging-id") public var packagingId: String = "",
  /**
   * The barcode, if the release has one, is a machine-readable number used as stock control
   * mechanisms by retailers. The most common types found on releases are 12-digit
   * UPCs and 13-digit EANs.
   */
  public var barcode: String = "",
  @field:Json(name = "cover-art-archive") @field:FallbackOnNull
  public var coverArtArchive: CoverArtArchive = NullCoverArtArchive,
  /**
   * See the
   * [page about disambiguation comments](https://musicbrainz.org/doc/Disambiguation_Comment)
   * for more information
   */
  public var disambiguation: String = "",
  /**
   * Data quality indicates how good the data for a release is. It is not a mark of how good or
   * bad the music itself is - for that, use ratings.
   * * **High quality**
   * All available data has been added, if possible including cover art with liner info that proves
   * it.
   * * **Default quality**
   * This is the default setting - technically "unknown" if the quality has never been modified,
   * "normal" if it has.
   * * **Low quality**
   * The release needs serious fixes, or its existence is hard to prove (but it's not clearly fake).
   */
  public var quality: String = "",
  /** The country the release was issued in. */
  public var country: String = "",
  public var asin: String = "",

  public var media: List<Medium> = emptyList(),
  /**
   * The label which issued the release. There may be more than one.
   * */
  @field:Json(name = "label-info") public var labelInfo: List<LabelInfo> = emptyList(),
  @field:Json(name = "track-count") public var trackCount: Int = 0,
  @field:Json(name = "status-id") public var statusId: String = "",
  /**
   * The language and script a release's track list is written in.
   */
  @field:Json(name = "text-representation") @field:FallbackOnNull
  public var textRepresentation: TextRepresentation = TextRepresentation.NullTextRepresentation,
  public var aliases: List<Alias> = emptyList(),
  /**
   * See the [page about annotations](https://musicbrainz.org/doc/Annotation) for more information.
   */
  public var annotation: String = "",
  public var genres: List<Genre> = emptyList(),
  public var tags: List<Tag> = emptyList(),
  @field:Json(name = "primary-type-id") public var primaryTypeId: String = "",
  @field:Json(name = "secondary-types") public var secondaryTypes: List<String> = emptyList(),
  @field:Json(name = "secondary-type-ids") public var secondaryTypeIds: List<String> = emptyList(),
  @field:Json(name = "first-release-date") public var firstReleaseDate: String = "",
  @field:FallbackOnNull public var rating: Rating = Rating.NullRating,
  /**
   * Types may be
   * * "nat"
   * * "album"
   * * "single"
   * * "ep"
   * * "compilation"
   * * "soundtrack"
   * * "spokenword"
   * * "interview"
   * * "audiobook"
   * * "live"
   * * "remix"
   * * "other"
   */
  @field:Json(name = "primary-type") public var primaryType: String = "",
  public var releases: List<Release> = emptyList(),
  public var relations: List<Relation> = emptyList(),
  /** Only relevant if returned from query */
  public var score: Int = 0
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Release

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = toJson()

  public interface Lookup : Include

  public enum class Subquery(override val value: String) : Lookup {
    Artists("artists"),
    Collections("collections"),
    Labels("labels"),
    Recordings("recordings"),
    ReleaseGroups("release-groups"),

    /** An ID calculated from the TOC of a CD */
    DiscIds("discids"),
    Media("media"),

    /**
     * The International Standard Recording Code, an identification system for audio and music
     * video recordings. Includes isrcs for all recordings
     */
    Isrcs("isrcs"),
    ArtistCredits("artist-credits"); // include artists credits for all releases and recordings
  }

  public enum class Misc(override val value: String) : Lookup {
    Aliases("aliases"),
    Annotation("annotation"),
    Tags("tags"),
    Ratings("ratings"),
    Genres("genres");

    public companion object {
      /** Doesn't create a values() array and/or list every time */
      public val all: List<Misc> by lazy { values().asList() }
    }
  }

  /**
   * Release relationships
   *
   * * [Release-Release](https://musicbrainz.org/relationships/release-release)
   * * [Release-Series](https://musicbrainz.org/relationships/release-series)
   * * [Release-URL](https://musicbrainz.org/relationships/release-url)
   */
  public enum class Relations(override val value: String) : Lookup {
    Release("release-rels"),
    Series("series-rels"),
    Url("url-rels"),
  }

  @Suppress("unused")
  public enum class Browse(override val value: String) : Lookup {
    ArtistCredits("artist-credits"),
    Labels("labels"),
    Recordings("recordings"),
    ReleaseGroups("release-groups"),
    Media("media"),
    DiscIds("discids"),
    Isrcs("isrcs"),
    Annotation("annotation"),
    Tags("tags"),
    Genres("genres"),
    Ratings("ratings")
  }

  @Suppress("unused")
  public enum class SearchField(public val value: String) {
    /** artist MusicBrainz id (MBID) */
    ArtistId("arid"),

    /** complete artist name(s) as it appears on the release */
    Artist("artist"),

    /** an artist on the release, each artist added as a separate field */
    ArtistName("artistname"),

    /** the Amazon ASIN for this release */
    Asin("asin"),

    /**
     * The barcode of this release which is a machine-readable number used as stock control
     * mechanisms by retailers.
     */
    Barcode("barcode"),

    /** The catalog number for this release, can have multiples when major using an imprint */
    CatalogNumber("catno"),

    /** Disambiguation comment */
    Comment("comment"),

    /** The two letter country code for the release country */
    Country("country"),

    /** name credit on the release, each artist added as a separate field */
    CreditName("creditname"),

    /** The release date (format: YYYY-MM-DD) */
    Date("date"),

    /** total number of cd ids over all mediums for the release */
    DiscIdCount("discids"),

    /** number of cd ids for the release on a medium in the release */
    MediumDiscCount("discidsmedium"),

    /** release format */
    Format("format"),

    /**
     * The label MusicBrainz id (MBID) for this release, a release can have multiples when
     * major using an imprint
     */
    LabelId("laid"),

    /** The name of the label for this release, can have multiples when major using an imprint */
    Label("label"),

    /**
     * The language for this release. Use the three character ISO 639-3 codes to search for a
     * specific language. (e.g. lang:eng)
     */
    Language("lang"),

    /** number of mediums in the release */
    MediumCount("mediums"),

    /** primary type of the release group (album, single, ep, other) */
    PrimaryType("primarytype"),

    /**
     * The release contains recordings with these puids
     *
     *  PUIDS are the IDs used in the proprietary MusicDNS audio fingerprinting system operated by
     *  MusicIP (used by MusicBrainz 2006–2013)
     */
    Puid("puid"),

    /** The quality of the release (low, normal, high) */
    Quality("quality"),

    /** release MusicBrainz id (MBID) */
    ReleaseId("reid"),

    /** release name */
    Release("release"),

    /** name of the release with any accent characters retained */
    ReleaseAccentedName("releaseaccent"),

    /** release group MusicBrainz id (MBID) */
    ReleaseGroupId("rgid"),

    /** The 4 character script code (e.g. latn) used for this release */
    Script("script"),

    /**
     * secondary type of the release group (audiobook, compilation, interview, live, remix,
     * soundtrack, spokenword)
     */
    SecondaryType("secondarytype"),

    /** release status (e.g official) */
    Status("status"),

    /** a tag that appears on the release */
    Tag("tag"),

    /** total number of tracks over all mediums on the release */
    TrackCount("tracks"),

    /** number of tracks on a medium in the release */
    MediumTrackCount("tracksmedium"),

    /** type of the release group, old type mapping for when we did not have separate primary
     * and secondary types
     */
    Type("type"),
  }

  /**
   * Used for any lookup which includes a [ReleaseGroup] or [Release] to limit results to
   * the that particular type of Release.
   */
  @Suppress("unused")
  public enum class Type(override val value: String) : Piped {
    Nat("nat"),
    Album("album"),
    Single("single"),
    Ep("ep"),
    Compilation("compilation"),
    Soundtrack("soundtrack"),
    SpokenWord("spokenword"),
    Interview("interview"),
    Audiobook("audiobook"),
    Live("live"),
    Remix("remix"),
    Other("other")
  }

  /**
   * Used for any lookup which includes a [Release] to limit results to that particular
   * [status][Release.status]
   */
  @Suppress("unused")
  public enum class Status(override val value: String) : Piped {
    Official("official"),
    Promotion("promotion"),
    Bootleg("bootleg"),
    PseudoRelease("pseudo-release")
  }

  public companion object {
    public val NullRelease: Release = Release(id = NullObject.ID)
    public val fallbackMapping: Pair<String, Any> = Release::class.java.name to NullRelease
  }
}

public inline val Release.isNullObject: Boolean
  get() = this === NullRelease

public inline class ReleaseMbid(override val value: String) : Mbid

public inline val Release.mbid: ReleaseMbid
  get() = id.toReleaseMbid()

@Suppress("NOTHING_TO_INLINE")
public inline fun String.toReleaseMbid(): ReleaseMbid {
  if (Mbid.logInvalidMbid && isInvalidMbid()) Timber.w("Invalid ReleaseMbid")
  return ReleaseMbid(this)
}

/** Assumes artist credit not present is exceptional */
@Suppress("unused")
public inline val Release.theArtistMbid: String
  get() {
    return try {
      artistCredit[0].artist.id
    } catch (e: Exception) {
      ""
    }
  }

/** Assumes artist credit not present is exceptional */
@Suppress("unused")
public inline val Release.theArtistName: String
  get() {
    return try {
      artistCredit[0].artist.name
    } catch (e: Exception) {
      ""
    }
  }

/** Assumes artist credit not present is exceptional */
@Suppress("unused")
public inline val Release.theArtistSortName: String
  get() {
    return try {
      artistCredit[0].artist.sortName
    } catch (e: Exception) {
      ""
    }
  }

public class PackagingInfo(public val id: PackagingMbid, public val name: String) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PackagingInfo

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}

@Suppress("unused")
public val Release.packagingInfo: PackagingInfo
  get() = PackagingInfo(packagingId.toPackagingMbid(), packaging)
