package com.minsu.springboot.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class) // 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 한다.
@WebMvcTest(controllers =  {HelloController.class}, secure = false) // 스프링 테스트 어노테이션 중, Web에 집중할 수 있는 어노테이션
@MockBean(JpaMetamodelMappingContext.class) // Test 안되는 오류 해결하기 위함
public class HelloControllerTest {

    @Autowired // 스프링이 관리하는 Bean을 주입 받는 어노테이션
    private MockMvc mvc; // 웹 API를 테스트 할 때 사용, 스프링 MVC 테스트의 시작점, 이 클래스를 통해 웹에 대한 API 테스트 가능

    @WithMockUser(roles = "USER")
    @Test
    public void return_hello() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello")) // ()안 주소로 HTTP GET 요청
                .andExpect(status().isOk()) // HTTP Header의 Status를 검증 ex) 200, 404, 500 등 상태 점검, mvc.perform의 결과를 검증할 때 .andExpect사용
                .andExpect(content().string(hello));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void result_helloDto() throws Exception {
        String name = "hello";
        int amount = 1000;
        mvc.perform(get("/hello/dto").param("name", name).param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name))) // JSON 응답값을 필드별로 검증할 수 있는 메소드
                .andExpect(jsonPath("$.amount", is(amount)));

    }

}
