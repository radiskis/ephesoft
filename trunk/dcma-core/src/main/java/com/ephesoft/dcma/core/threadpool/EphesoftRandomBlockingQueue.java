/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2012 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.core.threadpool;

/*
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is customized to get the random node from the Blocking Queue. This class is used for thread pool for picking random
 * threads from the queue.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see java.util.concurrent.BlockingQueue
 * @param <E>
 */
@SuppressWarnings("PMD")
public class EphesoftRandomBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, java.io.Serializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = -6903933977591709194L;

	/*
	 * A variant of the "two lock queue" algorithm. The putLock gates entry to put (and offer), and has an associated condition for
	 * waiting puts. Similarly for the takeLock. The "count" field that they both rely on is maintained as an atomic to avoid needing
	 * to get both locks in most cases. Also, to minimize need for puts to get takeLock and vice-versa, cascading notifies are used.
	 * When a put notices that it has enabled at least one take, it signals taker. That taker in turn signals others if more items have
	 * been entered since the signal. And symmetrically for takes signalling puts. Operations such as remove(Object) and iterators
	 * acquire both locks.
	 */

	/**
	 * Linked list node class.
	 */
	static class Node<E> {

		/** 
		 * The item, volatile to ensure barrier separating write and read .
		 */
		volatile E item;
		
		/**
		 * next Node<E>.
		 */
		Node<E> next;

		/**
		 * Constructor.
		 * @param node E
		 */
		Node(E node) {
			item = node;
		}
	}

	/** The capacity bound, or Integer.MAX_VALUE if none. */
	private final int capacity;

	/** Current number of elements. */
	private final AtomicInteger count = new AtomicInteger(0);

	/** Head of linked list. */
	private transient Node<E> head;

	/** Tail of linked list. */
	private transient Node<E> last;

	/** Lock held by take, poll, etc. */
	private final ReentrantLock takeLock = new ReentrantLock();

	/** Wait queue for waiting takes. */
	private final Condition notEmpty = takeLock.newCondition();

	/** Lock held by put, offer, etc. */
	private final ReentrantLock putLock = new ReentrantLock();

	/** Wait queue for waiting puts. */
	private final Condition notFull = putLock.newCondition();

	/**
	 * Signals a waiting take. Called only from put/offer (which do not otherwise ordinarily lock takeLock.).
	 */
	private void signalNotEmpty() {
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			notEmpty.signal();
		} finally {
			takeLock.unlock();
		}
	}

	/**
	 * Signals a waiting put. Called only from take/poll.
	 */
	private void signalNotFull() {
		final ReentrantLock putLock = this.putLock;
		putLock.lock();
		try {
			notFull.signal();
		} finally {
			putLock.unlock();
		}
	}

	/**
	 * Creates a node and links it at end of queue.
	 * 
	 * @param node the item
	 */
	private void insert(E node) {
		last = last.next = new Node<E>(node);
	}

	/**
	 * Removes a node from head of queue.
	 * 
	 * @return the node
	 */
	private E extract() {
		Random random = new Random();
		// Generate random number less the number of node in the queue.
		int randomInt = random.nextInt(count.get());
		if (randomInt < 0) {
			randomInt = Math.abs(randomInt);
		}
		// Store the head node.
		Node<E> first = head;
		// Traversing to the random node.
		for (int index = 0; index < randomInt; index++) {
			head = head.next;
		}
		Node<E> nodeToBeRemoved = head.next;
		head = first;
		// Storing random node item.
		E node = nodeToBeRemoved.item;
		remove(node);
		return node;
	}

	/**
	 * Lock to prevent both puts and takes.
	 */
	private void fullyLock() {
		putLock.lock();
		takeLock.lock();
	}

	/**
	 * Unlock to allow both puts and takes.
	 */
	private void fullyUnlock() {
		takeLock.unlock();
		putLock.unlock();
	}

	/**
	 * Creates a <tt>LinkedBlockingQueue</tt> with a capacity of {@link Integer#MAX_VALUE}.
	 */
	public EphesoftRandomBlockingQueue() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * Creates a <tt>LinkedBlockingQueue</tt> with the given (fixed) capacity.
	 * 
	 * @param capacity the capacity of this queue
	 */
	public EphesoftRandomBlockingQueue(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException();
		}
		this.capacity = capacity;
		last = head = new Node<E>(null);
	}

	/**
	 * Creates a <tt>LinkedBlockingQueue</tt> with a capacity of {@link Integer#MAX_VALUE}, initially containing the elements of the
	 * given collection, added in traversal order of the collection's iterator.
	 * 
	 * @param c the collection of elements to initially contain
	 * @throws NullPointerException if the specified collection or any of its elements are null
	 */
	public EphesoftRandomBlockingQueue(Collection<? extends E> c) {
		this(Integer.MAX_VALUE);
		for (E e : c) {
			add(e);
		}
	}

	// this doc comment is overridden to remove the reference to collections
	// greater in size than Integer.MAX_VALUE
	/**
	 * Returns the number of elements in this queue.
	 * 
	 * @return the number of elements in this queue
	 */
	public int size() {
		return count.get();
	}

	// this doc comment is a modified copy of the inherited doc comment,
	// without the reference to unlimited queues.
	/**
	 * Returns the number of additional elements that this queue can ideally (in the absence of memory or resource constraints) accept
	 * without blocking. This is always equal to the initial capacity of this queue less the current <tt>size</tt> of this queue.
	 * 
	 * <p>
	 * Note that you <em>cannot</em> always tell if an attempt to insert an element will succeed by inspecting
	 * <tt>remainingCapacity</tt> because it may be the case that another thread is about to insert or remove an element.
	 */
	public int remainingCapacity() {
		return capacity - count.get();
	}

	/**
	 * Inserts the specified element at the tail of this queue, waiting if necessary for space to become available.
	 * 
	 * @throws InterruptedException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	public void put(E e) throws InterruptedException {
		if (e == null) {
			throw new NullPointerException();
		}
		// Note: convention in all put/take/etc is to preset
		// local var holding count negative to indicate failure unless set.
		int c = -1;
		final ReentrantLock putLock = this.putLock;
		final AtomicInteger count = this.count;
		putLock.lockInterruptibly();
		try {
			/*
			 * Note that count is used in wait guard even though it is not protected by lock. This works because count can only
			 * decrease at this point (all other puts are shut out by lock), and we (or some other waiting put) are signalled if it
			 * ever changes from capacity. Similarly for all other uses of count in other wait guards.
			 */
			try {
				while (count.get() == capacity) {
					notFull.await();
				}
			} catch (InterruptedException ie) {
				notFull.signal(); // propagate to a non-interrupted thread
				throw ie;
			}
			insert(e);
			c = count.getAndIncrement();
			if (c + 1 < capacity) {
				notFull.signal();
			}
		} finally {
			putLock.unlock();
		}
		if (c == 0) {
			signalNotEmpty();
		}
	}

	/**
	 * Inserts the specified element at the tail of this queue, waiting if necessary up to the specified wait time for space to become
	 * available.
	 * 
	 * @return <tt>true</tt> if successful, or <tt>false</tt> if the specified waiting time elapses before space is available.
	 * @throws InterruptedException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {

		if (e == null)
			throw new NullPointerException();
		long nanos = unit.toNanos(timeout);
		int c = -1;
		final ReentrantLock putLock = this.putLock;
		final AtomicInteger count = this.count;
		putLock.lockInterruptibly();
		try {
			for (;;) {
				if (count.get() < capacity) {
					insert(e);
					c = count.getAndIncrement();
					if (c + 1 < capacity)
						notFull.signal();
					break;
				}
				if (nanos <= 0)
					return false;
				try {
					nanos = notFull.awaitNanos(nanos);
				} catch (InterruptedException ie) {
					notFull.signal(); // propagate to a non-interrupted thread
					throw ie;
				}
			}
		} finally {
			putLock.unlock();
		}
		if (c == 0)
			signalNotEmpty();
		return true;
	}

	/**
	 * Inserts the specified element at the tail of this queue if it is possible to do so immediately without exceeding the queue's
	 * capacity, returning <tt>true</tt> upon success and <tt>false</tt> if this queue is full. When using a capacity-restricted queue,
	 * this method is generally preferable to method {@link BlockingQueue#add add}, which can fail to insert an element only by
	 * throwing an exception.
	 * 
	 * @throws NullPointerException if the specified element is null
	 */
	public boolean offer(E e) {
		if (e == null)
			throw new NullPointerException();
		final AtomicInteger count = this.count;
		if (count.get() == capacity)
			return false;
		int c = -1;
		final ReentrantLock putLock = this.putLock;
		putLock.lock();
		try {
			if (count.get() < capacity) {
				insert(e);
				c = count.getAndIncrement();
				if (c + 1 < capacity)
					notFull.signal();
			}
		} finally {
			putLock.unlock();
		}
		if (c == 0)
			signalNotEmpty();
		return c >= 0;
	}

	/**
	 * Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
     * @throws NullPointerException if the specified element is null
     */
	public E take() throws InterruptedException {
		E x;
		int c = -1;
		final AtomicInteger count = this.count;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();
		try {
			try {
				while (count.get() == 0)
					notEmpty.await();
			} catch (InterruptedException ie) {
				notEmpty.signal(); // propagate to a non-interrupted thread
				throw ie;
			}

			x = extract();
			// count is already decrease while removing the node in extract method.
			c = count.get();
			if (c > 1)
				notEmpty.signal();
		} finally {
			takeLock.unlock();
		}
		if (c == capacity)
			signalNotFull();
		return x;
	}

	/**
	 * Retrieves and removes the head of this queue, waiting up to the specified wait time if necessary for an element to become available.
     *
     * @param timeout long
     * @param unit TimeUnit
     * @throws NullPointerException if the specified element is null
     */
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		E x = null;
		int c = -1;
		long nanos = unit.toNanos(timeout);
		final AtomicInteger count = this.count;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lockInterruptibly();
		try {
			for (;;) {
				if (count.get() > 0) {
					x = extract();
					c = count.getAndDecrement();
					if (c > 1)
						notEmpty.signal();
					break;
				}
				if (nanos <= 0)
					return null;
				try {
					nanos = notEmpty.awaitNanos(nanos);
				} catch (InterruptedException ie) {
					notEmpty.signal(); // propagate to a non-interrupted thread
					throw ie;
				}
			}
		} finally {
			takeLock.unlock();
		}
		if (c == capacity)
			signalNotFull();
		return x;
	}

	/**
	 * Retrieves and removes the head of this queue, or returns null if this queue is empty.
	 */
	public E poll() {
		final AtomicInteger count = this.count;
		if (count.get() == 0)
			return null;
		E x = null;
		int c = -1;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			if (count.get() > 0) {
				x = extract();
				c = count.getAndDecrement();
				if (c > 1)
					notEmpty.signal();
			}
		} finally {
			takeLock.unlock();
		}
		if (c == capacity)
			signalNotFull();
		return x;
	}

	/**
	 * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
	 */
	public E peek() {
		if (count.get() == 0)
			return null;
		final ReentrantLock takeLock = this.takeLock;
		takeLock.lock();
		try {
			Node<E> first = head.next;
			if (first == null)
				return null;
			else
				return first.item;
		} finally {
			takeLock.unlock();
		}
	}

	/**
	 * Removes a single instance of the specified element from this queue, if it is present. More formally, removes an element
	 * <tt>e</tt> such that <tt>o.equals(e)</tt>, if this queue contains one or more such elements. Returns <tt>true</tt> if this queue
	 * contained the specified element (or equivalently, if this queue changed as a result of the call).
	 * 
	 * @param o element to be removed from this queue, if present
	 * @return <tt>true</tt> if this queue changed as a result of the call
	 */
	public boolean remove(Object o) {
		if (o == null)
			return false;
		boolean removed = false;
		fullyLock();
		try {
			Node<E> trail = head;
			Node<E> p = head.next;
			while (p != null) {
				if (o.equals(p.item)) {
					removed = true;
					break;
				}
				trail = p;
				p = p.next;
			}
			if (removed) {
				p.item = null;
				trail.next = p.next;
				if (last == p)
					last = trail;
				if (count.getAndDecrement() == capacity)
					notFull.signalAll();
			}
		} finally {
			fullyUnlock();
		}
		return removed;
	}

	/**
	 * Returns an array containing all of the elements in this queue, in proper sequence.
	 * 
	 * <p>
	 * The returned array will be "safe" in that no references to it are maintained by this queue. (In other words, this method must
	 * allocate a new array). The caller is thus free to modify the returned array.
	 * 
	 * <p>
	 * This method acts as bridge between array-based and collection-based APIs.
	 * 
	 * @return an array containing all of the elements in this queue
	 */
	public Object[] toArray() {
		fullyLock();
		try {
			int size = count.get();
			Object[] a = new Object[size];
			int k = 0;
			for (Node<E> p = head.next; p != null; p = p.next)
				a[k++] = p.item;
			return a;
		} finally {
			fullyUnlock();
		}
	}

	/**
	 * Returns an array containing all of the elements in this queue, in proper sequence; the runtime type of the returned array is
	 * that of the specified array. If the queue fits in the specified array, it is returned therein. Otherwise, a new array is
	 * allocated with the runtime type of the specified array and the size of this queue.
	 * 
	 * <p>
	 * If this queue fits in the specified array with room to spare (i.e., the array has more elements than this queue), the element in
	 * the array immediately following the end of the queue is set to <tt>null</tt>.
	 * 
	 * <p>
	 * Like the {@link #toArray()} method, this method acts as bridge between array-based and collection-based APIs. Further, this
	 * method allows precise control over the runtime type of the output array, and may, under certain circumstances, be used to save
	 * allocation costs.
	 * 
	 * <p>
	 * Suppose <tt>x</tt> is a queue known to contain only strings. The following code can be used to dump the queue into a newly
	 * allocated array of <tt>String</tt>:
	 * 
	 * <pre>
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * String[] y = x.toArray(new String[0]);
	 * </pre>
	 * 
	 * Note that <tt>toArray(new Object[0])</tt> is identical in function to <tt>toArray()</tt>.
	 * 
	 * @param a the array into which the elements of the queue are to be stored, if it is big enough; otherwise, a new array of the
	 *            same runtime type is allocated for this purpose
	 * @return an array containing all of the elements in this queue
	 * @throws ArrayStoreException if the runtime type of the specified array is not a supertype of the runtime type of every element
	 *             in this queue
	 * @throws NullPointerException if the specified array is null
	 */
	public <T> T[] toArray(T[] a) {
		fullyLock();
		try {
			int size = count.get();
			if (a.length < size)
				a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);

			int k = 0;
			for (Node p = head.next; p != null; p = p.next)
				a[k++] = (T) p.item;
			if (a.length > k)
				a[k] = null;
			return a;
		} finally {
			fullyUnlock();
		}
	}

	/**
	 * Returns a string representation of this collection. 
	 * @return String
	 */
	public String toString() {
		fullyLock();
		try {
			return super.toString();
		} finally {
			fullyUnlock();
		}
	}

	/**
	 * Atomically removes all of the elements from this queue. The queue will be empty after this call returns.
	 */
	public void clear() {
		fullyLock();
		try {
			head.next = null;
			assert head.item == null;
			last = head;
			if (count.getAndSet(0) == capacity)
				notFull.signalAll();
		} finally {
			fullyUnlock();
		}
	}

	/**
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	public int drainTo(Collection<? super E> c) {
		if (c == null)
			throw new NullPointerException();
		if (c == this)
			throw new IllegalArgumentException();
		Node<E> first;
		fullyLock();
		try {
			first = head.next;
			head.next = null;
			assert head.item == null;
			last = head;
			if (count.getAndSet(0) == capacity)
				notFull.signalAll();
		} finally {
			fullyUnlock();
		}
		// Transfer the elements outside of locks
		int n = 0;
		for (Node<E> p = first; p != null; p = p.next) {
			c.add(p.item);
			p.item = null;
			++n;
		}
		return n;
	}

	/**
	 * Removes at most the given number of available elements from this queue and adds them to the given collection.
	 * 
	 * @param c Collection<? super E>
	 * @param maxElements int
	 * @return int
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 * @throws IllegalArgumentException {@inheritDoc}
	 */
	public int drainTo(Collection<? super E> c, int maxElements) {
		if (c == null)
			throw new NullPointerException();
		if (c == this)
			throw new IllegalArgumentException();
		fullyLock();
		try {
			int n = 0;
			Node<E> p = head.next;
			while (p != null && n < maxElements) {
				c.add(p.item);
				p.item = null;
				p = p.next;
				++n;
			}
			if (n != 0) {
				head.next = p;
				assert head.item == null;
				if (p == null)
					last = head;
				if (count.getAndAdd(-n) == capacity)
					notFull.signalAll();
			}
			return n;
		} finally {
			fullyUnlock();
		}
	}

	/**
	 * Returns an iterator over the elements in this queue in proper sequence. The returned <tt>Iterator</tt> is a "weakly consistent"
	 * iterator that will never throw {@link ConcurrentModificationException}, and guarantees to traverse elements as they existed upon
	 * construction of the iterator, and may (but is not guaranteed to) reflect any modifications subsequent to construction.
	 * 
	 * @return an iterator over the elements in this queue in proper sequence
	 */
	public Iterator<E> iterator() {
		return new Itr();
	}

	/**
	 * Iterator class to iterate on the list and retrieve elements.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	private class Itr implements Iterator<E> {

		/*
		 * Basic weak-consistent iterator. At all times hold the next item to hand out so that if hasNext() reports true, we will still
		 * have it to return even if lost race with a take etc.
		 */
		
		/**
		 * current Node<E>.
		 */
		private Node<E> current;
		
		/**
		 * lastRet Node<E>.
		 */
		private Node<E> lastRet;
		
		/**
		 * currentElement E.
		 */
		private E currentElement;

		/**
		 * Constructor.
		 */
		Itr() {
			final ReentrantLock putLock = EphesoftRandomBlockingQueue.this.putLock;
			final ReentrantLock takeLock = EphesoftRandomBlockingQueue.this.takeLock;
			putLock.lock();
			takeLock.lock();
			try {
				current = head.next;
				if (current != null)
					currentElement = current.item;
			} finally {
				takeLock.unlock();
				putLock.unlock();
			}
		}

		/**
		 * Returns true if the iteration has more elements. (In other words, returns true if next would return an element rather than throwing an exception.)
         * @return boolean
     	 */
		public boolean hasNext() {
			return current != null;
		}

		/**
		 * Returns the next element in the iteration.
         * return E
         */
		public E next() {
			final ReentrantLock putLock = EphesoftRandomBlockingQueue.this.putLock;
			final ReentrantLock takeLock = EphesoftRandomBlockingQueue.this.takeLock;
			putLock.lock();
			takeLock.lock();
			try {
				if (current == null)
					throw new NoSuchElementException();
				E x = currentElement;
				lastRet = current;
				current = current.next;
				if (current != null)
					currentElement = current.item;
				return x;
			} finally {
				takeLock.unlock();
				putLock.unlock();
			}
		}

		/**
		 * Removes from the underlying collection the last element returned by the iterator .
		 */
		public void remove() {
			if (lastRet == null)
				throw new IllegalStateException();
			final ReentrantLock putLock = EphesoftRandomBlockingQueue.this.putLock;
			final ReentrantLock takeLock = EphesoftRandomBlockingQueue.this.takeLock;
			putLock.lock();
			takeLock.lock();
			try {
				Node<E> node = lastRet;
				lastRet = null;
				Node<E> trail = head;
				Node<E> p = head.next;
				while (p != null && p != node) {
					trail = p;
					p = p.next;
				}
				if (p == node) {
					p.item = null;
					trail.next = p.next;
					if (last == p)
						last = trail;
					int c = count.getAndDecrement();
					if (c == capacity)
						notFull.signalAll();
				}
			} finally {
				takeLock.unlock();
				putLock.unlock();
			}
		}
	}

	/**
	 * Save the state to a stream (that is, serialize it).
	 * 
	 * @serialData The capacity is emitted (int), followed by all of its elements (each an <tt>Object</tt>) in the proper order,
	 *             followed by a null
	 * @param s the stream
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {

		fullyLock();
		try {
			// Write out any hidden stuff, plus capacity
			s.defaultWriteObject();

			// Write out all elements in the proper order.
			for (Node<E> p = head.next; p != null; p = p.next)
				s.writeObject(p.item);

			// Use trailing null as sentinel
			s.writeObject(null);
		} finally {
			fullyUnlock();
		}
	}

	/**
	 * Reconstitute this queue instance from a stream (that is, deserialize it).
	 * 
	 * @param s the stream
	 */
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
		// Read in capacity, and any hidden stuff
		s.defaultReadObject();

		count.set(0);
		last = head = new Node<E>(null);

		// Read in all elements and place in queue
		for (;;) {
			E item = (E) s.readObject();
			if (item == null)
				break;
			add(item);
		}
	}
}
