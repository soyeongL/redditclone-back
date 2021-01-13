package com.yeong.vege.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeong.vege.model.Post;
import com.yeong.vege.model.Subreddit;
import com.yeong.vege.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	List<Post> findAllBySubreddit(Subreddit subreddit);
	List<Post> findByUser(User user);
}
