package net.huadong.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBHelper {

	private static String URL = null;
	private static String USERNAME = null;
	private static String PASSWORD = null;

	private static Connection conn = null;
	private static Statement state = null;
	private static PreparedStatement preState = null;
	private static CallableStatement callState = null;
	private static ResultSet set = null;


	// 鏉堟挸鍤崹瀣畱閸欏倹鏆熼敍宀冪箷閺勵垵绶崙鍝勭�烽惃鍕棘閺侊拷
	// Parameter 閿涳拷 閼奉亜鐣炬稊澶屾畱閿涳拷 娴肩姴鍙嗛惃鍕棘閺侊拷 閸滐拷 缁鐎� 閿涳拷
	public static void execProcedure(String sql_call, Parameter... params) throws SQLException {
		openConn();
		callState = conn.prepareCall(sql_call);

		// 閸欏倹鏆熸稉宥呫偨婢跺嫮鎮�
		for (int i = 0; i < params.length; i++) {
			if (params[i].direction == Parameter.IN) {
				// 鐎佃妫╅張鐔鸿閸ㄥ娈戞径鍕倞
				/*
				 * if( params[i].getClass() == java.util.Date.class ) {
				 * java.util.Date d_temp = (java.util.Date)params[i];
				 * java.sql.Date d = new java.sql.Date(d_temp.getTime());
				 * 
				 * preState.setDate(i + 1, d); }else { preState.setObject(i + 1,
				 * params[i]); }
				 */
				callState.setObject(i + 1, params[i].param);
				System.out.println("数据："+params[i].param);
			} else if (params[i].direction == Parameter.OUT) {
				// 鐎电绶崙鍝勭�烽崣鍌涙殶閻ㄥ嫮琚崹瀣箻鐞涘苯顦╅悶锟�
				if (params[i].param.getClass() == Integer.class || params[i].param.getClass() == Double.class) {
					callState.registerOutParameter(i + 1, oracle.jdbc.OracleTypes.NUMBER);

				} else if (params[i].param.getClass() == String.class) {
					callState.registerOutParameter(i + 1, oracle.jdbc.OracleTypes.VARCHAR);
				} else {
					callState.registerOutParameter(i + 1, oracle.jdbc.OracleTypes.OTHER);
				}
			}

		}

		callState.execute();

		for (int i = 0; i < params.length; i++) {
			if (params[i].direction == Parameter.OUT) {
				Object obj_out = callState.getObject(i + 1);
				params[i].param = obj_out;
			}
		}

		closeAll();
	}

	// 閺屻儴顕楅弫鐗堝祦
	public static ResultSet execQuery(String sql) throws SQLException {
		openConn();

		state = conn.createStatement();
		set = state.executeQuery(sql);

		return set;
	}

	public static ResultSet execQuery(String sql, Object... params) throws SQLException {
		openConn();

		preState = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++) {
			if (params[i].getClass() == java.util.Date.class) {
				java.util.Date d_temp = (java.util.Date) params[i];
				java.sql.Date d = new java.sql.Date(d_temp.getTime());

				preState.setDate(i + 1, d);
			} else {
				preState.setObject(i + 1, params[i]);
			}

		}

		set = preState.executeQuery();

		return set;
	}

	// 涓嶈兘闃叉SQL娉ㄥ叆
	public static boolean execUpdate(String sql) throws SQLException {
		openConn();
		state = conn.createStatement();
		int record = state.executeUpdate(sql);
		closeAll();
		return record > 0;
	}

	// 鏉╂瑤閲滈弰顖濆厴闂冨弶顒汼QL濞夈劌鍙嗛惃鍕⒔鐞涳拷
	public static boolean execUpdate(String sql, Object... params) throws SQLException {
		openConn();
		preState = conn.prepareStatement(sql);

		for (int i = 0; i < params.length; i++) {
			// 閻╊喖澧犻惇瀣涧閺堝ate閺堝妫舵０锟�
			if (params[i].getClass() == java.util.Date.class) {
				java.util.Date d_temp = (java.util.Date) params[i];
				java.sql.Date d = new java.sql.Date(d_temp.getTime());

				preState.setDate(i + 1, d);
			} else {
				preState.setObject(i + 1, params[i]);
			}

		}

		int record = preState.executeUpdate();

		closeAll();
		return record > 0;
	}

	// 鎵撳紑鏁版嵁搴撹繛鎺�
	public static void openConn() throws SQLException {
		if (conn != null && conn.isClosed() == false) {
			// 濡傛灉杩炴帴宸茬粡鎵撳紑浜嗗氨娌℃湁鎿嶄綔
		} else {
			try {
				// 鑾峰彇閰嶇疆鏂囦欢涓殑鏁版嵁搴撹繛鎺ュ湴鍧�銆佺敤鎴峰悕鍜屽瘑鐮�
				Properties p = new Properties();
				InputStream is = Thread.currentThread().getContextClassLoader()
						.getSystemResourceAsStream("mySocket.properties");
				p.load(is);

				String url = p.getProperty("URL");
				String username = p.getProperty("USERNAME");
				String password = p.getProperty("PASSWORD");
				
				URL = url;
				USERNAME = username;
				PASSWORD = password;
	
				conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 鍏抽棴鍏ㄩ儴鎵撳紑鐨勮祫婧�
	public static void closeAll() {
		if (set != null) {
			try {
				set.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			set = null;
		}

		if (callState != null) {
			try {
				callState.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			callState = null;
		}

		if (preState != null) {
			try {
				preState.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			preState = null;
		}

		if (state != null) {
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			state = null;
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}

	
	
	/*public static void main(String[] args) {

		try {
			// boolean bln = execUpdate("delete from dept where deptno = 11");
			// boolean bln = execUpdate("delete from dept where deptno = ?" ,
			// 77);

			// 閸掋倖鏌囬弫鐗堝祦缁鐎� 閿涳拷2缁夊秵鏌熷蹇ョ礆
			
			 * Object d = new java.util.Date() ;
			 * System.out.println(d.getClass().getName()); if(
			 * d.getClass().getName().equals("java.util.Date") ) {
			 * System.out.println("閺勵垱妫╅張鐔鸿閸ㄥ绱�"); }
			 * 
			 * if(d.getClass() == java.util.Date.class) {
			 * System.out.println("閺勵垱妫╅張鐔鸿閸ㄥ绱�"); }
			 * 
			 * execUpdate("insert into emp(empno , ename , hiredate) values(?,?,?)"
			 * , 7777 , "閽勨剝鏋冩慨锟�" , new java.util.Date());
			 

			
			 * ResultSet set = execQuery("select * from dept where deptno = ?" ,
			 * 10 ); while(set.next()) { String dname = set.getString("dname");
			 * System.out.println( dname ); }
			 * 
			 * set.close(); set = null;
			 * 
			 * closeAll();
			 

			// 鐠嬪啰鏁ら弮鐘插棘閻ㄥ嫬鐡ㄩ崒銊ㄧ箖缁嬶拷
			// execProcedure("{call add_dept}");

			
			 * Parameter[] params = { 
			 * new Parameter(99) , 
			 * new Parameter("鐏忓繐宕犻柈锟�"),
			 * new Parameter("閸忋劌娴�") 
			 * 						};
			 *  execProcedure("{call usp_add_dept(?,?,?)}"
			 * , params);
			 

			Parameter[] params = { 
					new Parameter(99, Parameter.IN), 
					new Parameter("", Parameter.OUT) 
														};
//			execProcedure("{call usp_getSalaryByEmpno(?,?)}", params);
			execProcedure("{call usp_3(?,?)}", params);
			System.out.println(params[1].param.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

}
