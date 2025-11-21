import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './landing.html',
  styleUrls: ['./landing.scss'],
})
export class Landing implements OnInit, OnDestroy {
  servicios = [
    {
      img: 'https://www.direcional.com.br/wp-content/uploads/2025/03/closet-em-seu-apartamento-3-quartos.jpg',
      title: 'Closets a Medida',
      desc: 'Walk-in closets y closets empotrados optimizados a tu espacio.'
    },
    {
      img: 'https://www.mueblefacil.com/wp-content/uploads/2020/02/453-sala_final_1920.jpg',
      title: 'Muebles de TV',
      desc: 'Centros de entretenimiento modernos y funcionales.'
    },
    {
      img: 'https://melamina.net.pe/wp-content/uploads/2025/05/barra-de-melamina-para-cocina-negra.jpg',
      title: 'Cocinas Integrales',
      desc: 'Diseños de cocina de alta calidad con módulos completos.'
    },
    {
      img: 'https://melamina.net.pe/wp-content/uploads/2025/05/escritorios-de-melamine-para-adolescentes.jpg',
      title: 'Home Office',
      desc: 'Escritorios, repisas y estaciones de trabajo a medida.'
    },
    {
      img: 'https://expodeco.pe/uploads/174619917869902569.jpg',
      title: 'Muebles Personalizados',
      desc: 'Fabricación personalizada según tus diseños o referencias.'
    }
  ];

  currentIndex = 0;
  intervalId: any = null;

  transform = 'translateX(0%)';

  ngOnInit() {
    this.autoSlide();
  }

  autoSlide() {
    this.intervalId = setInterval(() => {
      this.currentIndex = (this.currentIndex + 1) % this.servicios.length;
      this.transform = `translateX(-${this.currentIndex * 100}%)`;  // ← SE ACTUALIZA
    }, 3500);
  }

  goToSlide(index: number) {
    this.currentIndex = index;
    this.transform = `translateX(-${index * 100}%)`;
  }

  ngOnDestroy() {
    if (this.intervalId) clearInterval(this.intervalId);
  }
}
