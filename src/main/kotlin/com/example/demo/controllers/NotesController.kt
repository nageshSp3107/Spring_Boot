package com.example.demo.controllers

import com.example.demo.database.model.Note
import com.example.demo.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

//POST http://localhost:8089/notes
//GET http://localhost:8089/notes?ownerId=123

@RestController
@RequestMapping("/notes")
class NotesController(
    private val repository: NoteRepository
) {

    data class NoteRequest(
        val id:String?,
        val title:String,
        val content:String,
        val color: Long
    )

    data class NoteResponse(
        val id: String,
        val title:String,
        val content:String,
        val color: Long,
        val createdAt: Instant
    )

    @PostMapping
    fun save(
        @RequestBody body: NoteRequest
    ): NoteResponse{
        val note = repository.save(Note(
            id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
            title = body.title,
            content = body.content,
            color = body.color,
            createdAt = Instant.now(),
            ownerId = ObjectId()
        )
        )
        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(
    @RequestParam(required = true) ownerId: String
    ): List<NoteResponse>{
        return repository.findByOwnerId(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }

    @GetMapping("notes")
    fun findNotes(): List<NoteResponse>{
        return repository.findAll().map {
            it.toResponse()
        }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
        @PathVariable id: String){
        repository.deleteById(ObjectId(id))
    }

    private fun Note.toResponse(): NotesController.NoteResponse{
        return NoteResponse(
            id = id.toHexString(),
            title = title,
            content = content,
            color = color,
            createdAt = createdAt
        )
    }
}