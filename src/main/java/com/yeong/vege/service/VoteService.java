package com.yeong.vege.service;

import static com.yeong.vege.model.VoteType.UPVOTE;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeong.vege.dto.VoteDto;
import com.yeong.vege.exceptions.PostNotFoundException;
import com.yeong.vege.exceptions.SpringRedditException;
import com.yeong.vege.model.Post;
import com.yeong.vege.model.Vote;
import com.yeong.vege.repository.PostRepository;
import com.yeong.vege.repository.VoteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {
	private final PostRepository postRepository;
	private final VoteRepository voteRepository;
	private final AuthService authService;
	
	
	@Transactional
	public void vote(VoteDto voteDto) {
		Post post = postRepository.findById(voteDto.getPostId())
				.orElseThrow(()->new PostNotFoundException("Post not dound with ID "+voteDto.getPostId()));
		Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
		if(voteByPostAndUser.isPresent() && 
				voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
			throw new SpringRedditException("You have already "+ voteDto.getVoteType() +" for this post");
		}
		if(UPVOTE.equals(voteDto.getVoteType())) {
			post.setVoteCount(post.getVoteCount()+1);
		}else {
			post.setVoteCount(post.getVoteCount()-1);
		}
		voteRepository.save(mapToVote(voteDto, post));
		postRepository.save(post);
	}
	
	private Vote mapToVote(VoteDto voteDto, Post post) {
		return Vote.builder()
				.voteType(voteDto.getVoteType())
				.post(post)
				.user(authService.getCurrentUser())
				.build();
	}
}
