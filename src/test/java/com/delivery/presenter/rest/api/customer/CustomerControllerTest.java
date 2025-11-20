package com.delivery.presenter.rest.api.customer;

import com.delivery.core.domain.Customer;
import com.delivery.core.domain.EmailAlreadyUsedException;
import com.delivery.core.entities.TestCoreEntityGenerator;
import com.delivery.core.usecases.customer.CreateCustomerUseCase;
import com.delivery.presenter.rest.api.common.BaseControllerTest;
import com.delivery.presenter.rest.api.entities.ApiResponse;
import com.delivery.presenter.rest.api.entities.AuthenticationResponse;
import com.delivery.presenter.rest.api.entities.SignInRequest;
import com.delivery.presenter.rest.api.entities.SignUpRequest;
import com.delivery.presenter.usecases.UseCaseExecutorImpl;
import com.delivery.core.usecases.UseCaseExecutor;
import com.delivery.presenter.usecases.security.AuthenticateCustomerUseCase;
import com.delivery.presenter.usecases.security.AuthenticateCustomerUseCaseInputMapper;
import com.delivery.presenter.usecases.security.CreateCustomerInputMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.concurrent.CompletableFuture;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import com.delivery.presenter.usecases.security.JwtProvider;
import com.delivery.presenter.usecases.security.CustomUserDetailsService;
import com.delivery.presenter.usecases.security.UserPrincipal;
import com.delivery.TestEntityGenerator;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest extends BaseControllerTest {

    @Configuration
    @ComponentScan(basePackages = {
            "com.delivery.presenter.rest.api.customer",
            "com.delivery.presenter.rest.api.common"
    })
    static class Config {
    }

    private JacksonTester<SignUpRequest> signUpJson;
    private JacksonTester<SignInRequest> signInJson;

    @MockBean
    private AuthenticateCustomerUseCase authenticateCustomerUseCase;

    @MockBean
    private CreateCustomerUseCase createCustomerUseCase;

    @MockBean
    private CreateCustomerInputMapper createCustomerInputMapper;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private UseCaseExecutor useCaseExecutor;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

        UserPrincipal userPrincipal = TestEntityGenerator.randomUserPrincipal();

        doReturn(userPrincipal)
                .when(userDetailsService)
                .loadUserById(userPrincipal.getId());

        doReturn(true)
                .when(jwtProvider)
                .validateToken(eq("token"));

        doReturn(userPrincipal.getId())
                .when(jwtProvider)
                .getUserIdFromToken(eq("token"));
    }

    @Override
    protected MockMvc getMockMvc() {
        return mockMvc;
    }

    @Test
    @Ignore
    public void signInReturnsOkWhenAuthenticationWorks() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest("email@email.com", "password");
        AuthenticateCustomerUseCase.InputValues input =
                AuthenticateCustomerUseCaseInputMapper.map(signInRequest);

        String token = "token";
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);

        String payload = signInJson.write(signInRequest).getJson();

        // and
        doReturn(CompletableFuture.completedFuture(ResponseEntity.ok(authenticationResponse)))
                .when(useCaseExecutor)
                .execute(
                        any(AuthenticateCustomerUseCase.class),
                        any(AuthenticateCustomerUseCase.InputValues.class),
                        any());

        // when
        RequestBuilder request = asyncPostRequest("/Customer/auth", payload);

        // then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Ignore
    public void signInReturnsBadRequestWhenAuthenticationManagerFails() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest("email@email.com", "password");
        String payload = signInJson.write(signInRequest).getJson();
        AuthenticateCustomerUseCase.InputValues input =
                AuthenticateCustomerUseCaseInputMapper.map(signInRequest);

        // and
        doReturn(CompletableFuture.completedFuture(ResponseEntity.badRequest().body(new ApiResponse(false, "Error"))))
                .when(useCaseExecutor)
                .execute(
                        any(AuthenticateCustomerUseCase.class),
                        any(AuthenticateCustomerUseCase.InputValues.class),
                        any());

        // when
        RequestBuilder request = asyncPostRequest("/Customer/auth", payload);

        // then
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", is("Error")));
    }

    @Test
    @Ignore
    public void signUpReturnsBadRequestWhenEmailIsAlreadyBeenUsed() throws Exception {
        // given
        final SignUpRequest signUpRequest = new SignUpRequest("name", "email@email.com", "address", "password");
        String payload = signUpJson.write(signUpRequest).getJson();
        CreateCustomerUseCase.InputValues inputValues = new CreateCustomerUseCase.InputValues(null, null, null, null);

        // and
        doReturn(CompletableFuture.completedFuture(ResponseEntity.badRequest().body(new ApiResponse(false, "Error"))))
                .when(useCaseExecutor)
                .execute(
                        any(CreateCustomerUseCase.class),
                        any(CreateCustomerUseCase.InputValues.class),
                        any());
        // when
        RequestBuilder request = asyncPostRequest("/Customer", payload);

        // then
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", is("Error")));
    }

    @Test
    @Ignore
    public void signUpReturnsCreatedWhenIsANewCustomer() throws Exception {
        // given
        final SignUpRequest signUpRequest = new SignUpRequest("name", "email@email.com", "address", "password");
        String payload = signUpJson.write(signUpRequest).getJson();
        Customer customer = TestCoreEntityGenerator.randomCustomer();

        CreateCustomerUseCase.InputValues input = new CreateCustomerUseCase.InputValues(
                customer.getName(), customer.getEmail(),
                customer.getAddress(), customer.getPassword());

        CreateCustomerUseCase.OutputValues output = new CreateCustomerUseCase.OutputValues(customer);

        // and
        doReturn(CompletableFuture.completedFuture(ResponseEntity.created(null).body(new ApiResponse(true, "registered successfully"))))
                .when(useCaseExecutor)
                .execute(
                        any(CreateCustomerUseCase.class),
                        any(CreateCustomerUseCase.InputValues.class),
                        any());

        // when
        RequestBuilder request = asyncPostRequest("/Customer", payload);

        // then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(header().string("location", "http://localhost/Customer/" + customer.getId().getNumber()))
                .andExpect(jsonPath("$.message", is("registered successfully")));
    }
}