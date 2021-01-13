package com.yeong.vege.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeong.vege.dto.PostRequest;
import com.yeong.vege.dto.PostResponse;
import com.yeong.vege.exceptions.PostNotFoundException;
import com.yeong.vege.exceptions.SubredditNotFoundException;
import com.yeong.vege.mapper.PostMapper;
import com.yeong.vege.model.Post;
import com.yeong.vege.model.Subreddit;
import com.yeong.vege.model.User;
import com.yeong.vege.repository.PostRepository;
import com.yeong.vege.repository.SubredditRepository;
import com.yeong.vege.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
	
	private final SubredditRepository subredditRepository;
	private final PostRepository postRepository;
	private final PostMapper postMapper;
	private final AuthService authService;
	private final UserRepository userRepository;
	
	public void save(PostRequest postRequest) {
		Subreddit subreddit  = subredditRepository.findByName(postRequest.getSubredditName())
				.orElseThrow(()->new SubredditNotFoundException(postRequest.getSubredditName()));
		postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
	}
	
	@Transactional(readOnly=true)
	public PostResponse getPost(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(()->new PostNotFoundException(id.toString()));
		return postMapper.mapToDto(post);
	}
	
	
	@Transactional(readOnly =true)
	public List<PostResponse> getAllPosts(){
		return postRepository.findAll()
				.stream().map(postMapper::mapToDto).collect(toList());
	}
	
	@Transactional(readOnly=true)
	public List<PostResponse> getPostsBySubreddit(Long subredditId){
		Subreddit subreddit = subredditRepository.findById(subredditId)
				.orElseThrow(()->new SubredditNotFoundException(subredditId.toString()));
		List<Post> posts = postRepository.findAllBySubreddit(subreddit);
		
		return posts.stream().map(postMapper::mapToDto).collect(toList());
	}
	
	@Transactional(readOnly=true)
	public List<PostResponse> getPostsByUsername(String username){
		User user = userRepository.findByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException(username));
		return postRepository.findByUser(user)
				.stream()
				.map(postMapper::mapToDto)
				.collect(toList());
	}
	
}
