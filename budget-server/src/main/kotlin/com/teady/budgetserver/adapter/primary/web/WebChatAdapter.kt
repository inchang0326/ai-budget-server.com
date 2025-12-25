//package com.teady.budgetserver.adapter.primary.web
//
//import com.teady.budgetserver.adapter.primary.web.port.WebChatAdapterPort
//import com.teady.budgetserver.application.dto.ChatDto
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/chat")
//class WebChatAdapter(
//    private val webChatAdapterPort: WebChatAdapterPort
//) {
//    @PostMapping("/question")
//    fun ask(@RequestBody chatDto: ChatDto): Map<String, String> {
//        return webChatAdapterPort.ask(chatDto)
//    }
//
//    @GetMapping("/rag-process")
//    fun prepare() {
//        webChatAdapterPort.prepare()
//    }
//
//    @PostMapping("/advanced-question")
//    fun askMore(@RequestBody chatDto: ChatDto): Map<String, String?> {
//        return webChatAdapterPort.askMore(chatDto)
//    }
//}