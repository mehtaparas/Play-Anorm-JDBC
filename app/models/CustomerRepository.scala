package models

import java.util.Date
import javax.inject.Inject

import scala.concurrent.Future

import anorm.SqlParser._
import anorm._

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.security.UserGroupInformation
// import org.apache.phoenix.jdbc.PhoenixDriver

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import java.security.PrivilegedExceptionAction
import java.sql._
import java.util.Properties
import java.io.FileInputStream

@javax.inject.Singleton
class CustomerRepository @Inject()(implicit ec: DatabaseExecutionContext) {

  System.setProperty("java.security.krb5.conf", "/home/apipoc/krb5.conf")
  System.setProperty("java.security.auth.login.config", "/home/parmehta/zookeeper_jaas.conf")
  // System.setProperty("sun.security.krb5.debug", "true")
  
  val conf = HBaseConfiguration.create();
  conf.addResource("/home/parmehta/configFiles/hbase-site.xml")
  conf.addResource("/home/parmehta/configFiles/hdfs-site.xml")
  conf.addResource("/home/parmehta/configFiles/core-site.xml")

  UserGroupInformation.setConfiguration(conf)
  UserGroupInformation.loginUserFromKeytab("bduet@HDP_DEV.COX.COM", "/home/apipoc/bduet.headless.keytab")
  val ugi = UserGroupInformation.getCurrentUser()

  val props = new Properties()
  props.load(new FileInputStream("/home/parmehta/Play-Anorm-JDBC/conf/hikaricp/phoenix.properties"))
 
  val hikariConfig = new HikariConfig()
  // hikariConfig.setDriverClassName(props.getProperty("driverClassName"))
  hikariConfig.setJdbcUrl(props.getProperty("jdbcUrl"))
  hikariConfig.setUsername(props.getProperty("username"))
  hikariConfig.setPassword(props.getProperty("password"))
  hikariConfig.setMaximumPoolSize(props.getProperty("poolSize").toInt)
  
  val dataSource = new HikariDataSource(hikariConfig)

  /**
  * dataSource.setDriverClassName("org.apache.phoenix.jdbc.PhoenixDriver")
  * dataSource.setJdbcUrl("jdbc:phoenix:dvtcbddd01.corp.cox.com,dvtcbddd02.corp.cox.com,dvtcbddd101.corp.cox.com:1521/hbase-secure")
  * dataSource.setUsername("test")
  * dataSource.setPassword("test")
  * dataSource.setMaximumPoolSize(15)
  */

  /**
   * Parse a Customer from a ResultSet
   */

  private val simple = {
    get[Int]("siteId") ~
      get[Int]("accountnbr") ~
      get[String]("node") ~
      get[String]("host") ~
      get[String]("headend") map {
      case siteId~accountnbr~node~host~headend =>
        Customer(siteId, accountnbr, node, host, headend)
    }
  }


  /**
   * Retrieve a customer from the accountNbr.
   */

  /**
  *
  * def findById(accountnbr: Int): Future[Option[Customer]] = Future {
  *  db.withConnection { implicit connection =>
  *    SQL("select * from customers where accountnbr = {accountnbr}").on('accountnbr -> accountnbr).as(simple.singleOpt)
  *  }
  * }(ec)
  */

  
  def findById(siteid: Int, accountnbr: Int): Future[Customer] = Future {

	val conn = ugi.doAs(new PrivilegedExceptionAction[Connection] {
		override def run(): Connection = {dataSource.getConnection}
		})
	
	/** val sqlArgs = accountnbr.split(',')
	* val site_id = sqlArgs(0)
	* val account = sqlArgs(1)
	*
	* val resultSet = conn.prepareStatement(s"select site_id, account_nbr, status, 1, 1 from UET.CI_ACCNT_EVENTS where SITE_ID = $site_id and ACCOUNT_NBR = $account limit 1").executeQuery()
	*/


	val resultSet = conn.prepareStatement(s"select * from UET.CI_ACCNT_NODE where SITE_ID = $siteid and ACCOUNT_NBR = $accountnbr").executeQuery()

	if(resultSet.next()) {
		val rs = Customer(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5))
		conn.close()
		rs
	}

	else {
		val rs = Customer(1,1,"1","1","1")
		conn.close()
		rs
	}
  }(ec)
}
