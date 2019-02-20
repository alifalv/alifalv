package com.common.filter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import com.common.cache.ICacheImpl;
import com.common.log.ExceptionLogger;

public class CachePageFilter implements Filter {
	//要缓存的api的uri
	static String[] cachePath= {
		"/api/article/listArticle/1/1/8",
		"/api/advice/adviceList/1/6",
		"/api/case/caseList/1/6",
		"/api/article/aliAdjudicationList/1/1/4",
		"/api/article/commonPeopleList/1/1/6",
		"/api/article/adviceQuestionList/1/1/8",
		"/api/article/questionAndViewpointList/1/1/8",
		"/api/article/bookMakeModelList/1/1/8",
		"/api/article/legalList/1/8"
	};
	
	static ICacheImpl cacheContent= ICacheImpl.getInstance();
	final static long CACHE_LOGNGTIME=1000*60*5;

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		String path=req.getRequestURI().replace(req.getContextPath(), "");
		String queryStr=req.getQueryString();
		queryStr=queryStr==null ||queryStr.trim().length()==0?"":"?"+queryStr;
		String key=path+queryStr;
		try {
			if(this.isContain(key)) {
				String content=(String)cacheContent.get(key);
				if(content!=null && content.length()>0 ) {
					response.setContentType("text/pain;charset=UTF-8");
					OutputStream os= response.getOutputStream();
					os.write(content.getBytes("UTF-8"));
					os.flush();
					os.close();
				}else {
					ResponseWrapper wrap=new ResponseWrapper((HttpServletResponse)response);
					chain.doFilter(request, wrap);
					wrap.getWriter().flush();
					wrap.getWriter().close();
					content=new String(wrap.getDataStream());
					cacheContent.put(key, content,this.CACHE_LOGNGTIME);
					OutputStream os= response.getOutputStream();

					os.write(content.getBytes());
					os.flush();
					os.close();
				}
				
			}else {
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			ExceptionLogger.writeLog(e,this.getClass());
		}
	}

	/**
	 * 看看配置中包括指定的路径不
	 * @param path
	 * @return
	 */
	private boolean isContain(String path) {
		for(String p : cachePath) {
			if(p.equals(path)) {
				return true;
			}
		}
		return false;
	}
    
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	
	}
	
	
	
	public class FilterServletOutputStream extends ServletOutputStream {
		DataOutputStream output;
		public FilterServletOutputStream(OutputStream output) {
			this.output = new DataOutputStream(output);
		}
		@Override
		public void write(int arg0) throws IOException {
			output.write(arg0);
		}
		@Override
		public void write(byte[] arg0, int arg1, int arg2) throws IOException {
			output.write(arg0, arg1, arg2);
		}
		@Override
		public void write(byte[] arg0) throws IOException {
			output.write(arg0);
		}
		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void setWriteListener(WriteListener arg0) {
			
		}
	}
	
	public class ResponseWrapper extends HttpServletResponseWrapper {
		ByteArrayOutputStream output;
		FilterServletOutputStream filterOutput;
		PrintWriter pw;
		public ResponseWrapper(HttpServletResponse response) {
			super(response);
			output = new ByteArrayOutputStream();
		}

		public PrintWriter getWriter() {
			if (filterOutput == null) {
				filterOutput = new FilterServletOutputStream(output);
				pw=new PrintWriter(filterOutput);
			}
			return pw;
			
		}
		
		public ServletOutputStream getOutputStream() throws IOException {
			if (filterOutput == null) {
				filterOutput = new FilterServletOutputStream(output);
			}
			return filterOutput;
		}
		public byte[] getDataStream() {
			return output.toByteArray();
		}
		
	}
	
	/**
     * Default constructor. 
     */
    public CachePageFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
