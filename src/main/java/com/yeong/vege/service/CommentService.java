package com.yeong.vege.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yeong.vege.dto.CommentsDto;
import com.yeong.vege.exceptions.PostNotFoundException;
import com.yeong.vege.mapper.CommentMapper;
import com.yeong.vege.model.Comment;
import com.yeong.vege.model.NotificationEmail;
import com.yeong.vege.model.Post;
import com.yeong.vege.model.User;
import com.yeong.vege.repository.CommentRepository;
import com.yeong.vege.repository.PostRepository;
import com.yeong.vege.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
	
	private static final String POST_URL="";
	private final PostRepository postRepository;
	private final CommentMapper commentMapper;
	private final AuthService authService;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	
	public void save(CommentsDto commentsDto) {
		Post post = postRepository.findById(commentsDto.getPostId())
				.orElseThrow(()->new PostNotFoundException(commentsDto.getPostId().toString()));
		Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
		commentRepository.save(comment);
		
		String message = mailContentBuilder.build(authService.getCurrentUser()+"posted a comment on your post"+ POST_URL);
		sendCommentNotification(message, post.getUser());
	}
	
	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(
				new NotificationEmail(user.getUsername()+" Commented on your post", user.getEmail(),message));
	}
	
	public List<CommentsDto> getAllCommentsForPost(Long postId){
		Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException(postId.toString()));
		return commentRepository.findByPost(post)
				.stream()
				.map(commentMapper::mapToDto).collect(toList());
	}
	
	public List<CommentsDto> getAllCommentsForUser(String userName){
		User user = userRepository.findByUsername(userName)
				.orElseThrow(()->new UsernameNotFoundException(userName));
		return commentRepository.findAllByUser(user)
				.stream()
				.map(commentMapper::mapToDto)
				.collect(toList());
	}
	
	
}
