package com.appleframework.cache.redis;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.appleframework.cache.codis.id.IdGenerator;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:config/apple-cache-codis-id.xml"})
public class IdGeneratorTest {

	@Resource
	private IdGenerator idGenerator;
	    
	@Test
	public void testAddOpinion1() {
		try {
			for (int i = 0; i < 1000; i++) {
				System.out.println(idGenerator.next("ddd", 100000000000L));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
     * ���̲߳������� 
     *  
     */ 
    @Test 
    public void MultiRequestsTest() {
        
    	// ����һ��Runner 
        TestRunnable runner = new TestRunnable() {
            @Override
            public void runTest() throws Throwable {
                // �������� 
            	System.out.println(idGenerator.next("ddd", 100000000000L));
            } 
        }; 
        int runnerCount = 100;
        
        //Rnner���飬�뵱�ڲ������ٸ��� 
        TestRunnable[] trs = new TestRunnable[runnerCount]; 
        for (int i = 0; i < runnerCount; i++) { 
            trs[i] = runner; 
        } 
        
        // ����ִ�ж��̲߳���������Runner����ǰ�涨��ĵ���Runner��ɵ����鴫�� 
        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs); 
        try { 
        	// ��������ִ�������ﶨ������� 
            mttr.runTestRunnables(); 
        } catch (Throwable e) { 
            e.printStackTrace(); 
        } 
    } 

}

