package net.sublime.rest.service.post

import net.sublime.rest.dto.post.PostDTO
import net.sublime.rest.repository.PostRepository

interface PostService {

    fun getAll(): List<PostDTO>
    fun getPost(id: Long): PostDTO
    fun createPost(post: PostDTO): PostDTO
    fun archivePost(post: PostDTO)
    fun archivePost(id: Long)
}
