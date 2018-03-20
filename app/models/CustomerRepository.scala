package models

import java.util.Date
import javax.inject.Inject

import scala.concurrent.Future

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.security.UserGroupInformation

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import java.security.PrivilegedExceptionAction
import java.sql._
import java.util.Properties
import java.io.FileInputStream

@javax.inject.Singleton
class CustomerRepository @Inject()(implicit ec: DatabaseExecutionContext) {

  val props = new Properties()
  props.load(new FileInputStream("/home/parmehta/Play-Anorm-JDBC/conf/hikaricp/phoenix.properties"))

  System.setProperty("java.security.krb5.conf", props.getProperty("kerbConf"))
  System.setProperty("java.security.auth.login.config", props.getProperty("zkJaas"))
  
  val conf = HBaseConfiguration.create();
  conf.addResource("hbase-site.xml")
  conf.addResource("hdfs-site.xml")
  conf.addResource("core-site.xml")

  UserGroupInformation.setConfiguration(conf)
  UserGroupInformation.loginUserFromKeytab(props.getProperty("kerbPrincipal"),props.getProperty("kerbKeytab"))
  val ugi = UserGroupInformation.getCurrentUser()

  val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(props.getProperty("jdbcUrl"))
  hikariConfig.setUsername(props.getProperty("username"))
  hikariConfig.setPassword(props.getProperty("password"))
  hikariConfig.setMaximumPoolSize(props.getProperty("poolSize").toInt)
  
  val dataSource = new HikariDataSource(hikariConfig)

 /**
  * Parse a Customer from a ResultSet
  *
  *
  * private val simple = {
  *  get[Int]("siteId") ~
  *    get[Int]("accountnbr") ~
  *    get[String]("node") ~
  *    get[String]("host") ~
  *    get[String]("headend") map {
  *    case siteId~accountnbr~node~host~headend =>
  *      Customer(siteId, accountnbr, node, host, headend)
  *  }
  * }
  *
  *
  * Retrieve a customer from the accountNbr.
  *
  *
  * def findById(accountnbr: Int): Future[Option[Customer]] = Future {
  *  db.withConnection { implicit connection =>
  *    SQL("select * from customers where accountnbr = {accountnbr}").on('accountnbr -> accountnbr).as(simple.singleOpt)
  *  }
  * }(ec)
  *
  */

  
  def findById(siteid: Int, accountnbr: Int): Future[Customer] = Future {

	val conn = ugi.doAs(new PrivilegedExceptionAction[Connection] {
		override def run(): Connection = {dataSource.getConnection}
		})
	
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
