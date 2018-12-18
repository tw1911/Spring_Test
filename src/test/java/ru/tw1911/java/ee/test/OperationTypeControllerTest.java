package ru.tw1911.java.ee.test;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.tw1911.java.ee.test.entity.OperationStage;
import ru.tw1911.java.ee.test.entity.OperationStageCode;
import ru.tw1911.java.ee.test.entity.OperationType;
import ru.tw1911.java.ee.test.entity.OperationTypeCode;
import ru.tw1911.java.ee.test.service.OperationStageCrudService;
import ru.tw1911.java.ee.test.service.OperationTypeCrudService;
import ru.tw1911.java.ee.test.utils.DtoMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationTypeControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MockMvc mockMvc;

    @Autowired
    private DtoMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    OperationTypeCrudService typeService;

    @MockBean
    OperationStageCrudService stageService;

    List<OperationType> types = new ArrayList<>();

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

    }

    @Before
    public void setUp(){

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        OperationType ot1 = new OperationType();
        ot1.setId(1L);
        ot1.setDateModified(Main.createLocalDateTime());
        ot1.setOperationType(OperationTypeCode.CREATE);
        ot1.setOperTypeNumber(1);
        ot1.setOperationTypeName("Operation1");
        ot1.setOrderIndex(1);

        OperationType ot2 = new OperationType();
        ot2.setId(2L);
        ot2.setDateModified(Main.createLocalDateTime());
        ot2.setOperationType(OperationTypeCode.UPDATE);
        ot2.setOperTypeNumber(2);
        ot2.setOperationTypeName("Operation2");
        ot2.setOrderIndex(10);

        OperationStage os1 = new OperationStage();
        os1.setId(1L);
        os1.setOperationStageName("Stage 1");
        os1.setOperStageCode(OperationStageCode.CREATED);
        OperationStage os2 = new OperationStage();
        os2.setId(2L);
        os2.setOperationStageName("Stage 2");
        os2.setOperStageCode(OperationStageCode.PROGRESS);
        OperationStage os3 = new OperationStage();
        os3.setId(3L);
        os3.setOperationStageName("Stage 3");
        os3.setOperStageCode(OperationStageCode.DONE);
        OperationStage os4 = new OperationStage();
        os4.setId(4L);
        os4.setOperationStageName("Stage 4");
        os4.setOperStageCode(OperationStageCode.ABORTED);

        List<OperationStage> stages= new ArrayList<>();
        Collections.addAll(stages,os1,os2,os3);
        List<OperationStage> stage2= new ArrayList<>();
        stage2.add(os4);
        List<OperationType> types = new ArrayList<>();
        Collections.addAll(types,ot1,ot2);
        types.forEach(type -> type.setOperStages(stages));
        stages.forEach(stage -> stage.setOperationTypes(types));
        this.types=types;
    }

    @Test
    public void readAllOperationTypeTest() throws Exception {
        when(typeService.readAll()).thenReturn(types);
        this.mockMvc.perform(get("/operations")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(types.get(0).getId()))
                .andExpect(jsonPath("$[0].operationType",is(types.get(0).getOperationType().toString())))
                .andExpect(jsonPath("$[0].operationTypeName",is(types.get(0).getOperationTypeName())))
                .andExpect(jsonPath("$[0].orderIndex",is(types.get(0).getOrderIndex())))
                .andExpect(jsonPath("$[1].id").value(types.get(1).getId()))
                .andExpect(jsonPath("$[1].operationType",is(types.get(1).getOperationType().toString())))
                .andExpect(jsonPath("$[1].operationTypeName",is(types.get(1).getOperationTypeName())))
                .andExpect(jsonPath("$[1].orderIndex",is(types.get(1).getOrderIndex())));
        verify(typeService, times(1)).readAll();
    }

    @Test
    public void readConcreateOperationTypeTest() throws Exception{
        long operationId = 1L;
        when(typeService.read(operationId)).thenReturn(types.get(0));
        this.mockMvc.perform(get("/operations/"+operationId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(types.get(0).getId()))
                .andExpect(jsonPath("$.operationType",is(types.get(0).getOperationType().toString())))
                .andExpect(jsonPath("$.operationTypeName",is(types.get(0).getOperationTypeName())))
                .andExpect(jsonPath("$.orderIndex",is(types.get(0).getOrderIndex())));
        verify(typeService, times(1)).read(operationId);
    }

    @Test
    public void createOperationTypeTest() throws Exception {
        OperationType type = types.get(0);
        ArgumentCaptor<OperationType> argument = ArgumentCaptor.forClass(OperationType.class);
        String requestJson = json(mapper.map(type));
        //String requestJson = "{\"id\": 5,\"operationType\": \"UPDATE\",\"operationTypeName\": \"Operation2\",\"orderIndex\": 10,\"dateModified\": \"2018-05-12T15:03:00.228\"}";
        when(typeService.create(any(OperationType.class))).thenReturn(type);
        this.mockMvc.perform(put("/operations").content(requestJson).contentType(contentType).with(user("user1")))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(type.getId()))
                .andExpect(jsonPath("$.operationType",is(type.getOperationType().toString())))
                .andExpect(jsonPath("$.operationTypeName",is(type.getOperationTypeName())))
                .andExpect(jsonPath("$.orderIndex",is(type.getOrderIndex())));
        verify(typeService, times(1)).create(argument.capture());

        assertThat(type.getId(),equalTo(argument.getValue().getId()));
        assertThat(type.getOperationType(),equalTo(argument.getValue().getOperationType()));
        assertThat(type.getOperationTypeName(),equalTo(argument.getValue().getOperationTypeName()));
        assertThat(type.getOrderIndex(),equalTo(argument.getValue().getOrderIndex()));

    }

    @Test
    public void updateOperationTypeTest() throws Exception{
        OperationType type = types.get(0);
        ArgumentCaptor<OperationType> argument = ArgumentCaptor.forClass(OperationType.class);
        String requestJson = json(mapper.map(type));
        //String requestJson = "{\"id\": 5,\"operationType\": \"UPDATE\",\"operationTypeName\": \"Operation2\",\"orderIndex\": 10,\"dateModified\": \"2018-05-12T15:03:00.228\"}";
        when(typeService.update(any(OperationType.class))).thenReturn(type);
        when(typeService.read(any(Long.class))).thenReturn(type);
        this.mockMvc.perform(post("/operations/"+type.getId()).content(requestJson).contentType(contentType).with(user("user1")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(type.getId()))
                .andExpect(jsonPath("$.operationType",is(type.getOperationType().toString())))
                .andExpect(jsonPath("$.operationTypeName",is(type.getOperationTypeName())))
                .andExpect(jsonPath("$.orderIndex",is(type.getOrderIndex())));
        verify(typeService, times(1)).update(argument.capture());

        argument.getValue().getInfo();
        assertThat(type.getId(),equalTo(argument.getValue().getId()));
        assertThat(type.getOperationType(),equalTo(argument.getValue().getOperationType()));
        assertThat(type.getOperationTypeName(),equalTo(argument.getValue().getOperationTypeName()));
        assertThat(type.getOrderIndex(),equalTo(argument.getValue().getOrderIndex()));    }

    @Test
    public void deleteOperationTest() throws Exception {
        OperationType type = types.get(0);
        this.mockMvc.perform(delete("/operations/"+type.getId())
                .with(user("user1")))
                .andExpect(status().isNoContent());
        verify(typeService,times(1)).delete(anyLong());
    }

    @Test
    public void readOperationTypeStagesTest() throws Exception {
        OperationType type = types.get(0);
        when(typeService.read(anyLong())).thenReturn(type);
        this.mockMvc.perform(get("/operations/1/stages")).andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    public void updateOperationTypeStagesTest() throws Exception {
        OperationType type = types.get(0);
        List<OperationStage> stages = type.getOperStages();
        String requestJson = json(stages.stream().map(stage -> mapper.map(stage)).collect(Collectors.toList()));
        stages.forEach(stage -> when(stageService.read(stage.getId())).thenReturn(stage));
        when(typeService.read(anyLong())).thenReturn(type);
        when(typeService.update(any(OperationType.class))).thenReturn(type);
        this.mockMvc.perform(post("/operations/"+type.getId()+"/stages").contentType(contentType).content(requestJson).with(user("user1"))).andExpect(status().isOk());

        verify(typeService,times(1)).update(any(OperationType.class));
        verify(stageService,times(3)).read(anyLong());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
