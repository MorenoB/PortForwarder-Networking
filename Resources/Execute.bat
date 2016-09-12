start "PortForwarder" java -jar PortForwarder.jar S 500 ServerFiles
start "PortForwarder" java -jar PortForwarder.jar S 501 ServerFiles
start "PortForwarder" java -jar PortForwarder.jar S 502 ServerFiles


start "PortForwarder" java -jar PortForwarder.jar P 80 localhost:500 localhost:501 localhost:502