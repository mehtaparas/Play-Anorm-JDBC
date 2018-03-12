package models

import java.util.Date
import javax.inject.Inject

import scala.concurrent.Future

import anorm.SqlParser._
import anorm._

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.security.UserGroupInformation
import org.apache.phoenix.jdbc.PhoenixDriver

import com.zaxxer.hikari.HikariDataSource

import java.security.PrivilegedExceptionAction
import java.sql._


@javax.inject.Singleton
class CustomerRepository @Inject()(implicit ec: DatabaseExecutionContext) {

  System.setProperty("java.security.krb5.conf", "/home/apipoc/krb5.conf")
  System.setProperty("java.security.auth.login.config", "/home/parmehta/zookeeper_jaas.conf")
  System.setProperty("sun.security.krb5.debug", "true")
  
  val conf = HBaseConfiguration.create();
  conf.addResource("/home/parmehta/configFiles/hbase-site.xml")
  conf.addResource("/home/parmehta/configFiles/hdfs-site.xml")
  conf.addResource("/home/parmehta/configFiles/core-site.xml")

  UserGroupInformation.setConfiguration(conf)
  UserGroupInformation.loginUserFromKeytab("bduet@HDP_DEV.COX.COM", "/home/apipoc/bduet.headless.keytab")
  val ugi = UserGroupInformation.getCurrentUser()

  val dataSource = new HikariDataSource()
  dataSource.setDriverClassName("org.apache.phoenix.jdbc.PhoenixDriver")
  dataSource.setJdbcUrl("jdbc:phoenix:dvtcbddd01.corp.cox.com,dvtcbddd02.corp.cox.com,dvtcbddd101.corp.cox.com:1521/hbase-secure")
  dataSource.setUsername("test")
  dataSource.setPassword("test")


  /**
   * Parse a Customer from a ResultSet
   */

  private val simple = {
    get[Int]("accountnbr") ~
      get[Int]("siteId") ~
      get[Int]("video") ~
      get[Int]("voice") ~
      get[Int]("data") map {
      case accountnbr~siteId~video~voice~data =>
        Customer(accountnbr, siteId, video, voice, data)
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

  
  def findById(accountnbr: Int): Future[Customer] = Future {

	val conn = ugi.doAs(new PrivilegedExceptionAction[Connection] {
		override def run(): Connection = {dataSource.getConnection}
		})

	val resultSet = conn.prepareStatement(s"select * from UET.CUSTOMERS where ACCOUNTNBR = $accountnbr limit 1").executeQuery()
      
	if(resultSet.next()) {
		val rs = Customer(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5))
		conn.close()
		rs
	}

	else {
		val rs = Customer(1,1,1,1,1)
		conn.close()
		rs
	}
  }(ec)
}
