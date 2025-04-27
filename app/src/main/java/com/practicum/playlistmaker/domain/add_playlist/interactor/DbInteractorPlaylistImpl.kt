package com.practicum.playlistmaker.domain.add_playlist.interactor


import com.practicum.playlistmaker.data.audioplayer.entity.PlayListAndTrackEntity
import com.practicum.playlistmaker.domain.add_playlist.DbInteractorPlaylist
import com.practicum.playlistmaker.domain.add_playlist.DbRepositoryPlaylist
import com.practicum.playlistmaker.domain.add_playlist.PicRepository
import com.practicum.playlistmaker.domain.add_playlist.models.PlaylistWithTrackCount
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.models.PlayListAndTrack
import com.practicum.playlistmaker.ui.library.add_playlist.models.Playlist
import com.practicum.playlistmaker.ui.library.info_playlist.models.PlaylistInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DbInteractorPlaylistImpl(
    private val dbRepositoryPlaylist: DbRepositoryPlaylist,
    private val picRepository: PicRepository
) :
    DbInteractorPlaylist {

    override fun insertPlaylist(playlist: Playlist): Flow<Boolean> = flow {
        val id = dbRepositoryPlaylist.insertPlaylist(playlist)
        playlist.image?.let { picRepository.savePic(it, id.toString()) }

        emit(true)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistWithTrackCount>> =
        dbRepositoryPlaylist.getAllPlaylists().map { list ->
            list.map { playlistWithTrackCount ->
                PlaylistWithTrackCount(
                    id = playlistWithTrackCount.id,
                    title = playlistWithTrackCount.title,
                    image = picRepository.getPic(playlistWithTrackCount.id.toString()),
                    trackCount = playlistWithTrackCount.trackCount
                )
            }
        }

    override fun addTrackToPlaylist(
        track: Track,
        playlistAndTrack: PlayListAndTrack
    ): Flow<Boolean> = flow {
        val result = dbRepositoryPlaylist.addTrackToPlaylist(
            playlistAndTrack = playlistAndTrack,
            track = track
        )
        if (result == -1L) {
            emit(false)
            return@flow
        }
        emit(true)
    }

    override fun getPlaylistInfoById(id: Long): Flow<PlaylistInfo?> =
        dbRepositoryPlaylist.getPlaylistInfoById(id).map { response ->
            if (response == null){
                return@map null
            }

            val pic = picRepository.getPic(id.toString())
            PlaylistInfo(
                title = response.title,
                description = response.description,
                image = pic,
                count = response.count,
                duration = response.duration
            )
        }


    override fun getTrackInPlaylist(id: Long): Flow<List<Track>>  =
      dbRepositoryPlaylist.getTracksInPlaylist(id)



    override suspend fun deleteTrack(track: Track, playlistAndTrack: PlayListAndTrack) {
        dbRepositoryPlaylist.deleteTrack(track, playlistAndTrack.mapToEntity())
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        dbRepositoryPlaylist.updatePlaylist(playlist)
        playlist.image?.let { picRepository.savePic(it, playlist.id.toString()) }
    }

    override suspend fun deletePlaylist(id: Long) {
        val tracks = dbRepositoryPlaylist.getTracksInPlaylist(id).first()
        tracks.forEach { it->
            val playListAndTrack = PlayListAndTrack(
                    playlistId = id,
                    trackId = it.trackId
                )
            dbRepositoryPlaylist.deleteTrack(it, playListAndTrack.mapToEntity())
        }


        dbRepositoryPlaylist.deletePlaylist(id)
        picRepository.deletePic(id.toString())
    }


}

private fun PlayListAndTrack.mapToEntity(): PlayListAndTrackEntity {
    return PlayListAndTrackEntity(
        playlistId = playlistId,
        trackId = trackId
    )
}
