all:
	javac -cp lib/gson-2.13.1.jar -d out src/*.java
clean:
	rm out/*.class
