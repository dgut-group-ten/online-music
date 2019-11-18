package com.groupten.online_music.dao.impl;

import com.groupten.online_music.entity.SongList;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISongList
        extends JpaSpecificationExecutor<SongList>, PagingAndSortingRepository<SongList, Integer> {
}
