package com.teady.aibudgetserver.adapter.secondary.llm

import com.teady.aibudgetserver.adapter.secondary.llm.port.MyRAGClientPort
import com.teady.aibudgetserver.application.dto.ChatDto
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor
import org.springframework.ai.reader.ExtractedTextFormatter
import org.springframework.ai.reader.pdf.PagePdfDocumentReader
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.net.MalformedURLException

@Slf4j
@RequiredArgsConstructor
@Service
class MyRAGClient (
  private val vectorStore: VectorStore,
  private val chatClient: ChatClient,
  private val retrievalAugmentationAdvisor: RetrievalAugmentationAdvisor
) : MyRAGClientPort {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(MyRAGClient::class.java)
    }

    @Throws(MalformedURLException::class)
    override fun processedRAG() {
        val pdfResource: Resource = ClassPathResource("static/spring-boot-reference.pdf")
        // Spring AI utility class to read a PDF file page by page
        // Extract
        val pdfReader = PagePdfDocumentReader(
            pdfResource,
            PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(
                    ExtractedTextFormatter.builder()
                        .withNumberOfBottomTextLinesToDelete(3) // Specifies that the bottom 3 lines of text on each page should be deleted.
                        .withNumberOfTopPagesToSkipBeforeDelete(1) // Indicates that the text deletion rule should not apply to the first page.
                        .build()
                )
                .withPagesPerDocument(1)
                .build()
        )

        // Transform
        val tokenTextSplitter = TokenTextSplitter()
        log.info("Parsing document, splitting, creating embeddings, and storing in vector store...")

        // tag as external knowledge in the vector store's metadata
        val splitDocuments = tokenTextSplitter.split(pdfReader.read())
        for (splitDocument in splitDocuments) { // footnotes
            splitDocument.metadata["filename"] = pdfResource.filename
            splitDocument.metadata["version"] = 1
        }

        // Sending batch of documents to vector store
        // Load
        vectorStore.write(splitDocuments)
        log.info("Done parsing document, splitting, creating embeddings and storing in vector store.")
    }

    override fun answerAdvanced(chatDto: ChatDto): String? {
        return chatClient.prompt()
            .advisors(retrievalAugmentationAdvisor)
            .user(chatDto.message)
            .call()
            .content()
    }
}