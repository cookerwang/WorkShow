###[mockito学习](https://gojko.net/2009/10/23/mockito-in-six-easy-examples/)

	package com.kutec.domain;
	
	import org.hamcrest.BaseMatcher;
	import org.hamcrest.Description;
	import org.junit.Assert;
	import org.junit.Before;
	import org.junit.Test;
	import org.mockito.Mock;
	import org.mockito.Mockito;
	
	import java.io.IOException;
	import java.io.OutputStream;
	import java.io.OutputStreamWriter;
	import java.util.Iterator;

	/**
	 * Author: wangrenxing(wangrenxing87@gmail.com)
	 * Date: 2016/3/3.
	 */
	public class MockitoTest {
	    @Mock
	    private OutputStream mockOS;
	    @Before
	    public void setUp() {
	        // 构建mock对象或初始化实例
	    }
	
	    @Test
	    public void test_iterator_will_return_hello_world() {
	        Iterator mockIterator = Mockito.mock(Iterator.class);
	        Mockito.when(mockIterator.next()).thenReturn("hello").thenReturn("world");
	        final String result = mockIterator.next() + " " + mockIterator.next();
	
	        Assert.assertEquals("hello world", result);
	    }
	
	    @Test
	    public void test_with_arguments() {
	        Comparable c = Mockito.mock(Comparable.class);
	        Mockito.when(c.compareTo("hello")).thenReturn(1);
	        Assert.assertEquals(1, c.compareTo("hello"));
	    }
	
	    @Test
	    public void test_with_unspecified_arguments() {
	        Comparable mockC = Mockito.mock(Comparable.class);
	        Mockito.when(mockC.compareTo(Mockito.anyInt())).thenReturn(-1);
	        Assert.assertEquals(-1, mockC.compareTo(9999));
	    }
	
	    @Test(expected = IOException.class)
	    public void test_OutputStreamWriter_rethrows_an_exception_from_OutputStream() throws IOException{
	        OutputStream mockOs = Mockito.mock(OutputStream.class);
	        OutputStreamWriter osw = new OutputStreamWriter(mockOs);
	        Mockito.doThrow(IOException.class).when(mockOs).close();
	        mockOs.close();
	    }
	
	    @Test
	    public void test_OutputStreamWriter_Closes_OutputStream_on_Close() throws IOException {
	        OutputStream mockOs = Mockito.mock(OutputStream.class);
	        OutputStreamWriter osw = new OutputStreamWriter(mockOs);
	        osw.close();
	        Mockito.verify(mockOs).close();// 检查mockOs是否调用close()
	    }
	
	    @Test
	    public void test_OutputStreamWriter_Buffers_And_Forwards_To_OutputStream() throws IOException {
	        OutputStream mockOs = Mockito.mock(OutputStream.class);
	        OutputStreamWriter osw = new OutputStreamWriter(mockOs);
	        osw.write('A');
	        osw.flush();
	        // can't do this as we don't know how long the array is going to be
	        // verify(mock).write(new byte[]{'a'},0,1);
	        //Mockito.verify(mockOs).write(new byte[]{'A'}); // error
	        BaseMatcher<byte[]> arrayStartingWithA = new BaseMatcher<byte[]>(){
	            @Override
	            public void describeTo(Description description) {
	                // nothing
	            }
	            // check that first character is A
	            @Override
	            public boolean matches(Object item) {
	                byte[] actual=(byte[]) item;
	                return actual[0]=='A';
	            }
	        };
	        // check that first character of the array is A, and that the other two arguments are 0 and 1
	        Mockito.verify(mockOs).write(Mockito.argThat(arrayStartingWithA), Mockito.eq(0),Mockito.eq(1));
	    }
	
	}
	
