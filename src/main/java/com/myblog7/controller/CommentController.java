package com.myblog7.controller;

import com.myblog7.payload.CommentDto;
import com.myblog7.payload.PostDto;
import com.myblog7.repositry.CommentRepository;
import com.myblog7.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    //htpp:localhost:8080/api/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto>createComment(@PathVariable(value = "postId")long postId, @RequestBody CommentDto commentDto ){
        return  new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
  public List<CommentDto> getCommentByPostId(@PathVariable(value = "postId")long postId){
        return commentService.getCommentsByPostId(postId);
  }


    @GetMapping("/posts/{postId}/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable(value = "postId")long postId,
                                     @PathVariable(value = "commentId")long commentId){
        return commentService.getCommentsById(postId,commentId);
    }

    @GetMapping("/comments")
    public List<CommentDto> getAllCommentsById(){
        return commentService.getAllCommentsById();
    }


    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable(value = "postId") long postId,
                                    @PathVariable(value = "commentId") long commentId
    ) {
        commentService.deleteCommentById(postId,commentId);
        return new ResponseEntity<>("Comment is deleted",HttpStatus.OK) ;
    }
//
//
//
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<CommentDto> updatePost(@PathVariable("id") long id, @RequestBody CommentDto commentDto){
//        CommentDto dto = commentService.updateComment(id, commentDto);
//        return  new ResponseEntity<>(dto,HttpStatus.OK);
//    }



}
