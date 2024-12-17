package com.example.yum_web_server.ingredient.controller

import com.example.yum_web_server.ingredient.dto.IngredientResponseDto
import com.example.yum_web_server.ingredient.enums.IngredientCategory
import com.example.yum_web_server.ingredient.service.IngredientService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class IngredientControllerTest(
    @Autowired private val webTestClient: WebTestClient,
) : DescribeSpec({
    val ingredientService = mockk<IngredientService>()
    val url = "/api/ingredients"
    describe("/api/ingredients로 GET 요청을 하는 경우에") {
        coroutineDebugProbes = true
        afterTest {
            clearAllMocks()
        }
        context("이미 저장된 재료가 2개 있다면") {
            val ingredient1 = IngredientResponseDto(
                id = 1,
                name = "egg",
                isFreezed = false,
                isFavorite = false,
                category = IngredientCategory.egg,
                startAt = LocalDate.of(2024, 11, 12),
                endAt = LocalDate.of(2024, 11, 19),
            )
            val ingredient2 = IngredientResponseDto(
                id = 2,
                name = "beef",
                isFreezed = false,
                isFavorite = false,
                category = IngredientCategory.beef,
                startAt = LocalDate.of(2024, 11, 12),
                endAt = LocalDate.of(2024, 11, 19),
            )
            coEvery { ingredientService.getMyIngredient() } returns listOf(ingredient1, ingredient2)
            val result = webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
            it("200의 응답코드가 반환된다.") {
                result
                    .expectStatus().isOk
            }
            it("2개의 재료가 반환된다.") {
                result
                    .expectBodyList<IngredientResponseDto>()
            }
        }
        context("저장된 재료가 없다면") {
            coEvery { ingredientService.getMyIngredient() } returns emptyList()
            val result = webTestClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
            it("200의 응답코드를 반환한다.") {
                result
                    .expectStatus().isOk
            }
            it("빈 배열을 반환한다.") {
                result
                    .expectBody().json("[]")
            }
        }
    }
    describe("/api/ingredients로 POST 요청을 하는 경우에") {
        coroutineDebugProbes = true
        afterTest {
            clearAllMocks()
        }
        context("새로운 재료를 추가할 때") {
            val newIngredient = JSONObject()
                .put("name", "")
                .put("isFreezed", false)
                .put("category", "")
                .put("startAt", "")
                .put("endAt","")
                .toString()
            val result = webTestClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newIngredient)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
            it("Validation을 통과하지 못하면 400 에러가 반환된다.") {
                result
                    .expectStatus().isBadRequest
            }
            it("이름이 없으면 \"이름을 입력하세요!\"가 반환된다.") {
                result
                    .expectBody()
                    .jsonPath("$['data']._name").isEqualTo("이름을 입력하세요!")
            }
            it("카테고리가 잘못설정되었다면 \"잘못된 재료 카테고리입니다!\"가 반환된다.") {
                result
                    .expectBody()
                    .jsonPath("$['data']._category").isEqualTo("잘못된 재료 카테고리입니다!")
            }
            it("날짜형식이 잘못되었다면 \"잘못된 날짜형식입니다!\"가 반환된다.") {
                result
                    .expectBody()
                    .jsonPath("$['data']._startAt").isEqualTo("잘못된 날짜형식입니다!")
                    .jsonPath("$['data']._endAt").isEqualTo("잘못된 날짜형식입니다!")
            }
        }

        context("새로운 재료가 추가된다면") {
            val newIngredient = JSONObject()
                .put("name", "egg")
                .put("isFreezed", false)
                .put("category", "egg")
                .put("startAt", "2024-11-12")
                .put("endAt","2024-11-19")
                .toString()
            coEvery { ingredientService.createIngredient(any()) } returns IngredientResponseDto(
                id = 1,
                name = "egg",
                isFreezed = false,
                isFavorite = false,
                category = IngredientCategory.egg,
                startAt = LocalDate.of(2024, 11, 12),
                endAt = LocalDate.of(2024, 11, 19)
            )
            val result = webTestClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newIngredient)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
            it("201의 응답코드를 반환한다.") {
                result
                    .expectStatus().isCreated
            }
            it("IngredientResponseDto를 반환한다.") {
                result
                    .expectBody<IngredientResponseDto>()
            }
            it("새로 추가된 재료를 응답한다.") {
                result
                    .expectBody()
                    .jsonPath("$.name").isEqualTo("egg")
                    .jsonPath("$.isFavorite").isEqualTo(false)
                    .jsonPath("$.isFreezed").isEqualTo(false)
                    .jsonPath("$.startAt").isEqualTo("2024-11-12")
                    .jsonPath("$.endAt").isEqualTo("2024-11-19")
            }
        }
    }
})
//    @Autowired
//    private lateinit var webTestClient : WebTestClient
//
//    @MockkBean
//    private lateinit var ingredientService : IngredientService
//
//

//    @BeforeEach
//    fun setup() : Unit = runBlocking {
//        coEvery { ingredientService.getMyIngredient() } returns listOf(ingredient1, ingredient2)
//        coEvery { ingredientService.createIngredient(any()) } returns ingredient1
//    }
//
//    /**
//     * 재료 조회 컨트롤러 테스트
//     */
//    @Test
//    fun `사용자 재료 조회 테스트 성공시 응답코드 200 반환한다`() : Unit = runTest {

//    }
//
//    @Test
//    fun `사용자 재료 조회 성공시 DB에서 사용자가 등록한 모든 재료를 가져온다`() : Unit = runTest {
//        webTestClient.get()
//            .uri("/api/ingredients")
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .expectBody()
//            .jsonPath("$[0].name").isEqualTo("egg")
//            .jsonPath("$[1].name").isEqualTo("beef")
//            .jsonPath("$[0].startAt").isEqualTo("2024-11-12")
//    }
//
//    /**
//     * 재료 생성 테스트
//     */
//    @Test
//    fun `사용자 재료 생성 성공시 응답코드 201을 반환한다`() : Unit = runTest {
//        val newIngredient = IngredientRequestDto(
//            "egg", false, "egg", "2024-11-12", "2024-11-19"
//        )
//        webTestClient.post()
//            .uri("/api/ingredients")
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(newIngredient)
//            .exchange()
//            .expectStatus().isCreated
//            .expectBody()
//    }
//
//    @Test
//    fun `사용자 재료 생성 성공시 생성된 재료가 반환된다`() : Unit = runTest {

//        webTestClient.post()
//            .uri("/api/ingredients")
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(newIngredient)
//            .exchange()
//            .expectBody()
//            .jsonPath("$.name").isEqualTo("egg")
//            .jsonPath("$.isFreezed").isEqualTo(false)
//            .jsonPath("$.isFavorite").isEqualTo(false)
//            .jsonPath("$.category").isEqualTo("egg")
//            .jsonPath("$.startAt").isEqualTo("2024-11-12")
//            .jsonPath("$.endAt").isEqualTo("2024-11-19")
//    }
//
//    @Test
//    fun `사용자 재료 생성시 이름이 없어선 안된다 이름을 입력하지 않으면 에러메시지가 반환된다`() : Unit = runTest {
//        val newIngredient = IngredientRequestDto(
//            "", false, "egg", "2024-11-12", "2024-11-19"
//        )
//        webTestClient.post()
//            .uri("/api/ingredients")
//            .accept(MediaType.APPLICATION_JSON)
//            .bodyValue(newIngredient)
//            .exchange()
//            .expectStatus().isBadRequest
//            .expectBody()
//            .jsonPath("$['data']._name").isEqualTo("이름을 입력하세요!")
//    }

//    @Test
//    fun `사용자 재료 생성시 카테고리가 올바르지 못하면 "잘못된 재료 카테고리입니다!"메세지가 반환된다`() : Unit = runTest {
//        val newIngredient = IngredientRequestDto(
//            "", false, "egg", "2024-11-12", "2024-11-19"
//        )
//        webTestClient.post()
//            .uri("/api/ingredients")
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(newIngredient)
//            .exchange()
//            .expectStatus().isBadRequest
//            .expectBody()
//            .jsonPath("$['data']._category").isEqualTo("잘못된 재료 카테고리입니다!")
//    }
//
//    @Test
//    fun `사용자 재료 생성시 카테고리가 비어있으면 카테고리를 "카테고리를 입력하세요!"메세지가 반환된다`() : Unit = runTest {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/ingredients").content(
//            """
//                {
//                    "name": "egg",
//                    "isFreezed": false,
//                    "category": "",
//                    "startAt": "2024-11-15",
//                    "endAt": "2024-11-16"
//                }
//            """.trimIndent()
//        ).contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isBadRequest)
//            .andExpect(
//                jsonPath("$['data']._category").value("카테고리를 입력하세요!")
//        )
//    }
//
//    @Test
//    fun `사용자 재료 생성시 날짜 형식이 잘못되면 "잘못된 날짜형식입니다!"메세지가 반환된다`() : Unit = runTest {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/ingredients").content(
//            """
//                {
//                    "name": "egg",
//                    "isFreezed": false,
//                    "category": "egg",
//                    "startAt": "2024-1115",
//                    "endAt": "2024-11-46"
//                }
//            """.trimIndent()
//        ).contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isBadRequest)
//            .andExpect(
//                jsonPath("$['data']._startAt").value("잘못된 날짜형식입니다!")
//            )
//            .andExpect(
//                jsonPath("$['data']._endAt").value("잘못된 날짜형식입니다!")
//            )
//    }
