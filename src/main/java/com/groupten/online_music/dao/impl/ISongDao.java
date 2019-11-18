package com.groupten.online_music.dao.impl;

import com.groupten.onlinemusic.entity.Song;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISongDao
        extends JpaSpecificationExecutor<Song>, PagingAndSortingRepository<Song, Integer> {
}
