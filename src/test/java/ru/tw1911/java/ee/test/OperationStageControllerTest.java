package ru.tw1911.java.ee.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationStageControllerTest {
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

    List<OperationStage> stages = new ArrayList<>();

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
        this.stages=stages;
    }

    @Test
    public void createOperationStageTest() throws Exception {
        OperationStage stage = stages.get(0);
        String requestJson = json(mapper.map(stage));
        when(stageService.create(any(OperationStage.class))).thenReturn(stage);
        this.mockMvc.perform(put("/stages/").content(requestJson).contentType(contentType).with(user("user1")))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(stage.getId()))
                .andExpect(jsonPath("$.operStageCode",is(stage.getOperStageCode().toString())))
                .andExpect(jsonPath("$.operationStageName",is(stage.getOperationStageName())))
                .andExpect(jsonPath("$.dateModified",is(stage.getDateModified())));
    }

    @Test
    public void readOperationStageTest() throws Exception {
        OperationStage stage = stages.get(0);
        when(stageService.read(anyLong())).thenReturn(stage);
        this.mockMvc.perform(get("/stages/"+stage.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(stage.getId()))
                .andExpect(jsonPath("$.operStageCode",is(stage.getOperStageCode().toString())))
                .andExpect(jsonPath("$.operationStageName",is(stage.getOperationStageName())))
                .andExpect(jsonPath("$.dateModified",is(stage.getDateModified())));
        verify(stageService,times(1)).read(stage.getId());
    }

    @Test
    public void readAllOperationStageTest() throws Exception {
        when(stageService.readAll()).thenReturn(stages);
        this.mockMvc.perform(get("/stages/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$",hasSize(3)));
        verify(stageService,times(1)).readAll();
    }

    @Test
    public void updateOperationStageTest() throws Exception {
        OperationStage stage = stages.get(0);
        String requestJson = json(mapper.map(stage));
        when(stageService.read(anyLong())).thenReturn(stage);
        when(stageService.update(any(OperationStage.class))).thenReturn(stage);
        this.mockMvc.perform(post("/stages/"+stage.getId()).content(requestJson).contentType(contentType).with(user("user1")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(stage.getId()))
                .andExpect(jsonPath("$.operStageCode",is(stage.getOperStageCode().toString())))
                .andExpect(jsonPath("$.operationStageName",is(stage.getOperationStageName())))
                .andExpect(jsonPath("$.dateModified",is(stage.getDateModified())));
        verify(stageService,times(1)).read(anyLong());
        verify(stageService,times(1)).update(any(OperationStage.class));
    }

    @Test
    public void deleteOperationStageTest() throws Exception {
        OperationStage stage = stages.get(0);
        this.mockMvc.perform(delete("/stages/"+stage.getId()).with(user("user1")))
                .andExpect(status().isNoContent());
        verify(stageService, Mockito.times(1)).delete(anyLong());
    }

    @Test
    public void readOperationTypesInStageTest() throws Exception {
        OperationStage stage = stages.get(0);
        List<OperationType> types = stage.getOperationTypes();
        when(stageService.read(anyLong())).thenReturn(stage);
        this.mockMvc.perform(get("/stages/"+stage.getId()+"/types").with(user("user1")))
                .andExpect(status().isOk());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
