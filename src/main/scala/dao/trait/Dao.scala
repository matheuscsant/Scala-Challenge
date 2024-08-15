package dao.`trait`

trait Dao[T] {
  def findById(id: Long): T

  def findAll: List[T]

  def update(id: Long, entity: T): Unit

  def delete(id: Long): Unit

  def insert(entity: T): Long
}
