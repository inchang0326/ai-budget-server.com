//package com.teady.budgetserver.adapter.secondary.llm
//
//import org.springframework.ai.chat.client.ChatClient
//import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor
//import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
//import org.springframework.ai.ollama.OllamaChatModel
//import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter
//import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever
//import org.springframework.ai.vectorstore.VectorStore
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//
//@Configuration
//class MyRAGConfig(
//    private val ollamaChatModel: OllamaChatModel
//) {
//    @Bean
//    fun chatClient(): ChatClient {
//        return ChatClient.builder(ollamaChatModel)
//            .defaultAdvisors(SimpleLoggerAdvisor())
//            .build()
//    }
//
//    @Bean
//    fun retrievalAugmentationAdvisor(vectorStore: VectorStore): RetrievalAugmentationAdvisor {
//        val documentRetriever = VectorStoreDocumentRetriever.builder()
//            .vectorStore(vectorStore)
//            .similarityThreshold(0.50)
//            .topK(5)
//            .build()
//        val queryAugmenter = ContextualQueryAugmenter.builder()
//            .allowEmptyContext(true)
//            .build()
//        return RetrievalAugmentationAdvisor.builder()
//            .documentRetriever(documentRetriever)
//            .queryAugmenter(queryAugmenter)
//            .build()
//    }
//}