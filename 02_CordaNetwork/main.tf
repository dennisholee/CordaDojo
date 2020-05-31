provider "google" {
  project = var.project_id
}

locals {
  app           = "corda"
  terraform     = "terraform"
  zone          = "us-central1-a"
  region        = "us-central1"
  ip_cidr_range = "192.168.0.0/24"
}

# -------------------------------------------------------------------------------
# Network
# -------------------------------------------------------------------------------

resource "google_compute_network" "vpc" {
  name                    = "${local.app}-network"
  auto_create_subnetworks = "false"
}

resource "google_compute_subnetwork" "subnet" {
  name                     = "${local.app}-subnet"
  region                   = local.region
  ip_cidr_range            = local.ip_cidr_range
  network                  = "${google_compute_network.vpc.self_link}"
  private_ip_google_access = true
}

# -------------------------------------------------------------------------------
# Public IP Address
# -------------------------------------------------------------------------------

resource "google_compute_address" "notary-address" {
  name         = "${local.app}-notary-address"
  region       = "${local.region}"
}

resource "google_compute_address" "nodea-address" {
  name         = "${local.app}-nodea-address"
  region       = "${local.region}"
}

resource "google_compute_address" "nodeb-address" {
  name         = "${local.app}-nodeb-address"
  region       = "${local.region}"
}

# -------------------------------------------------------------------------------
# Firewall
# -------------------------------------------------------------------------------

resource "google_compute_firewall" "firewall" {
  name          = "fw-${local.app}"
  network       = "${google_compute_network.vpc.self_link}"
  direction     = "INGRESS"
  source_ranges = ["0.0.0.0/0"]
  
  allow {
    protocol = "tcp"
    ports    = ["22","5005", "5006", "5007"]
  }

  target_tags = ["fw-${local.app}"]
}

# -------------------------------------------------------------------------------
# Compute Engine
# -------------------------------------------------------------------------------

resource "google_compute_instance" "server" {
  name                      = "${local.app}-server"
  machine_type              = "n1-standard-2"
  zone                      = "${local.zone}"
  count                     = 1
  
  metadata = {
    sshKeys  = "dennislee:${file("${var.public_key}")}"
  }

  service_account {
    scopes  = ["cloud-platform"]
  }

  network_interface {
    subnetwork = "${google_compute_subnetwork.subnet.self_link}"
    access_config {
        nat_ip = "${google_compute_address.notary-address.address}"
    }
  }

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-9"
    }
  }

  metadata_startup_script = <<SCRIPT
sudo apt-get update
sudo apt install -y wget openjdk-8-jdk vim unzip git

cd ~/
wget https://services.gradle.org/distributions/gradle-5.4.1-bin.zip

mkdir /opt/gradle
cp gradle-5.4.1-bin.zip /opt/gradle
cd /opt/gradle
unzip gradle-5.4.1-bin.zip

cat <<EOF >>/home/dennislee/.profile
export GRADLE_HOME=/opt/gradle/gradle-5.4
export PATH=/opt/gradle/gradle-5.4/bin:$PATH
EOF

SCRIPT

  tags = ["${google_compute_firewall.firewall.name}"] 
}

