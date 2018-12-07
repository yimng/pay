package com.edupay;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;  

@RunWith(SpringJUnit4ClassRunner.class)    
@WebAppConfiguration    
@ContextConfiguration({"classpath*:/applicationContext.xml","classpath*:/spring-mvc.xml"})   
//当然 你可以声明一个事务管理 每个单元测试都进行事务回滚 无论成功与否    
@TransactionConfiguration(defaultRollback = true)    
@Transactional   
public class TestController2 {  
    @Autowired    
    private WebApplicationContext wac;    
    
    private MockMvc mockMvc;   
      
    @Before    
    public void setup() {     
        this.mockMvc = webAppContextSetup(this.wac).build();    
    }   
      
    @Test    
    public void testLogin() throws Exception {    
        mockMvc.perform((get("/loginTest").param("name", "admin"))).andExpect(status().isOk())    
                .andDo(print());    
    }   
      
    /*@Test   
    //有些单元测试你不希望回滚   
    @Rollback(false)   
    public void testInsert() throws Exception {   
        mockMvc.perform((post("/insertTest"))).andExpect(status().isOk())   
                .andDo(print());   
    } */  
}  